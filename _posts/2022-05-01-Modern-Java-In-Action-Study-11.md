---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter7(2)"
date:   2022-04-25T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd

---



# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter7-(2)

## 병렬 데이터 처리와 성능

### 포크/조인 프레임워크

병렬화 할 수 있는 작업을 재귀적으로 작은 작업으로 분할한 뒤 작은 작업들의 결과를 합쳐서 전체 결과를 만들도록 설계되어있다. 포크/조인 프레임워크에서는 작은 작업들을 스레드 풀(Fork Join Pool)의 작업자 스레드에 분살 할당하는 `ExecutorService` 인터페이스를 구현한다.

#### RecursiveTask 활용

`RecursiveTask<R>`의 서브클래스를 구현해야 스레드 풀을 이용할 수 있다. `RecursiveTask`를 정의하기 위해서는 추상 메서드인 `compute`를 구현해야 한다. 대부분 아래와 같은 의사코드 형식을 따른다

```seudo
if (태스크가 충분이 작거나 더 이상 분할할 수 없으면) {
  순차적을 태스크 계산
} else {
  태스크를 두 서브 태스크로 분할
  태스크가 다시 서브 태스크로 분활되도록 이 메서드를 재귀적으로 호출
  모든  서브태스크의 연산이 완료될 때까지 기다림
  각 서브태스크의 결과를 합침
}
```

**포크/조인 프레임워크를 이용한 병렬 합계 수행 코드**

```java
public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    private final long[] numbers; // 더할 숫자 배열
    private final int start; // 이 서브 태스크에서 처리할 배열의 초기위치와 최종 위치
    private final int end;
    private static final long THRESHOLD = 10_000; // 이 값 이하의 서브태스크는 더 이상 분할 X

    public ForkJoinSumCalculator(long[] numbers) {  // 메인 태스크를 생성할 때 사용할 공개 생성자
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) { // 메인 태스크의 서브태스크를 재귀적으로 만들때 사용할 비공개 생성자
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if(length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2); // 배열 첫번째 절반 더하도록 서브태스크 생성
        leftTask.fork(); // ForkJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기로 실행
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute(); // 두번째 서브태스크를 동기 실행한다. 이때 추가 분할이 일어날 수 있다.
        Long leftResult = leftTask.join(); // 첫번째 태스크의 결과를 읽거나 아직 결과가 없으면 기다린다.
        return leftResult + rightResult; // 두 결과를 조합하여 최종 결과를 반환한다.
    }
    
    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
```

`ForkJoinPool`은 일반적으로 필요한 곳에서 언제든 가져다 쓸 수 있도록 한번만 인스턴스화 해서 정적 필드에 싱글톤으로 저장한다. `fork()` 를 호출해서 인수가 없는 디폴트 생성자를 이용했는데, 이는 JVM에서 이용할 수 있는 모든 프로세서가 자유롭게 풀에 접근할 수 있음을 의미한다.

**ForkJoinSumCalculator 실행**

`ForkJoinSumCalculator`를 `ForkJoinPool`로 전달하면 풀의 스레드가 `compute()`메서드를 호출한다. 이후 위의 의사코드처럼 부분 결과를 합쳐서 최종 결과를 계산한다.

- 포크/조인 알고리즘 과정

![CamScanner 05-01-2022 14 31n_1](https://user-images.githubusercontent.com/78407939/166133606-9b1c4de5-3688-4b7b-96bb-cb86eea9a35b.jpg)

#### 포크/조인 프레임워크를 제대로 사용하는 방법

- `join()`메서드를 호출하면 태스크가 생산하는 결과가 준비될 때까지 호출자를 **블록**시킨다. 따라서 두 서브태스크가 모두 시작된 다음에 `join()`을 호출해야 한다. 그렇지 않으면 각각의 서브태스크가 다른 태스크가 끝나길 기다리는 일이 발생하며 원래의 순차 알고리즘보다 느리고 복잡한 프로그램이 된다.

> *블록이란? :*  어떤 요청이 발생하고 완료될 때까지 모든 일을 중단한 상태로 대기하는 것을 블로킹 방식이라고 한다. 즉 결과가 준비될 때까지 다른 작업을 중단하고 하염없이 기다리는 것을 의미한다.

- `RecursiveTask` 내에서는 순차 코드에서 병렬 계산을 시작할 떄를 제외하고는 `ForkJoinPool`의 `invoke()` 메서드를 사용하지 말아야한다. 대신에 `compute()`나 `fork()` 는 직접 호출할 수 있다.
- 서브태스크의 `fork()` 메서드를 호출해서 `ForkJoinPool`의 일정을 조절할 수 있다. 왼쪽과 오른쪽 모두 `fork()`를 호출하는 것이 효율적일 것 같지만, 한쪽에서만 `fork()`를 호출하고 나머지 한쪽은 `compute()`를 호출하는 것이 더 효율적이다. 그래야만 두 서브태스크의 한 태스크에서는 같은 스레드를 재사용할 수 있으므로 풀에서 불필요한 태스크를 할당하는 오버헤드를 피할 수 있다.
- 포크/조인 프레임워크를 이용한 병렬 계산은 디버깅이 어렵다.
- 무조건 멀티코어에 포크/조인 프레임워크 사용이 빠를 것이라는 생각은 버려야 한다. 병렬 처리로 성능을 개선하려면 태스크를 여러 독립적인 서브태스크로 분리할 수 있어야 한다. 

#### 작업 훔치기

포크/조인 프레임워크에서는 **작업 훔치기**라는 기법으로 주어진 서브태스크를 더 분할할 것인지 결정할 기준을 정하는데 도움을 준다. 작업 훔치기를 통해서 `ForkJoinPool`의 모든 스레드를 거의 공정하게 분할한다.

- 각각의 스레드는 자신에게 할당된 태스크를 포함하는 이중 연결 리스트를 참조한다.
- 작업이 끝날때마다 큐의 **헤드**에서 다른 태스크를 가져와서 작업을 처리한다.
- 만약 한 스레드가 다른 스레드보다 작업을 빨리 끝냈을 경우 기다리는 것이 아니라, 다른 스레드 큐의 **꼬리**에서 작업을 훔쳐와서 처리한다. 이 과정을 모든 큐가 빌때까지 즉, 모든 작업을 처리할 때까지 반복한다.

따라서 태스크 크기를 작게 나누어야 스레드간 작업 부하를 비슷한 수준으로 유지할 수 있다.



### Spliterator 인터페이스

`Spliterator`는 `Iterator` 처럼 소스 요소 탐색 기능을 제공한다는 점은 같지만, 병렬화에 특화되어 있다는 점에서 차이점이 있다. `Spliterator`는 다음과 같은 여러 메서드를 정의한다.

```java
public interface Spliterator<T> {
  boolean tryAdvance(Consumer<? super T> action);
  Spliterator<T> trySplit();
  long estimateSize();
  int characteristics();
}
```

- `tryAdavance`

  요소를 하나씩 순차적으로 소비하면서 탐색할 요소가 남아있다면 `true`를 반환한다.

- `trySplit()`

  `Spliterator`의 일부 요소를 분할해서 두 번째 `Spliterator`를 만든다

- `estimateSize()`

  탐색해야 할 요소의 수에 대한 값을 반환

#### 분할 과정

1. `trySplit()` 첫번째 호출로 인해 두 개의 `Spliterator`가 된다
2. 다시 `trySplit()` 를 호출하면 4개의 `Spliterator`가 된다
3. `trySplit()`의 결과가 null이 될 때까지 이 과정을 반복한다.

**Spliterator 특성**

- `characteristics()`

  `Spliterator` 자쳉의 특성 집합을 포함하는 `int`를 반환한다. `Spliterator`를 이용하는 프로그램은 이들 특성을 참고해서 `Spliterator`를 더 잘 제어하고 최적화 할 수 있다. 

  > *Spliterator의 특성*
  >
  > - ORDERED
  >
  >   요소에 정해진 순서가 있으므로 탐색, 분할 할때 이 순서에 따라야 한다.
  >
  > - DISTINCT
  >
  >   x, y 두 요소를 방문했을때 x.equals(y)는 항상 false를 반환한다.
  >
  > - SORTED
  >
  >   탐색된 요소는 미리 정의된 정렬 순서를 따른다.
  >
  > - SIZED
  >
  >   `estimatedSize()`는 정확한 값을 반환한다.
  >
  > - NON-NULL
  >
  >   탐색하는 모든 요소는 null이 아니다.
  >
  > - IMMUTABLE
  >
  >   불변이다. 요소를 탐색하는 동안 요소를 추가하거나, 삭제하거나, 변경할 수 없다.
  >
  > - CONCURRENT
  >
  >   동기화 없이 `Spliterator`의 소스를 여러 스레드에서 동시에 고칠 수 있다.
  >
  > - SUBSIZED
  >
  >   분할되는 모든 `Spliterator`까지 모두 SIZED 특성을 갖는다.

#### 커스텀 Spliterator 구현하기

**반복형으로 단어 수를 세는 메서드**

```java
public class Main {
    public static void main(String[] args) {
        System.out.println(countWordsIteratively("채상엽 채상엽 채상엽"));
    }

    public static int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if(lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }
}
```

```
3
```

**함수형으로 단어 수를 세는 메서드 재구현하기**

반복형 대신 함수형을 사용하면 직접 스레드를 동기화하지 않고 병렬 스트림으로 작업을 병렬화할 수 있다. 먼저 `String`을 스트림으로 바꾸어주어야 한다.

```java
Stream<Character> stream = IntStream.range(0, SENTENCE.length())
  .mapToObj(SENTENCE::charAt);
```

```java
public class WordCounter {
    private final int counter;
    private final boolean lastSpace;

    public WordCounter(int counter, boolean lastSpace) {
        this.counter = counter;
        this.lastSpace = lastSpace;
    }

    public WordCounter accumulator(Character c) {
        if (Character.isWhitespace(c)) {
            return lastSpace ? this : new WordCounter(counter, true);
        } else {
            return lastSpace ? new WordCounter(counter + 1, false) : this;
        }
    }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }
    
    public int getCounter() {
        return counter;
    }
}
```

```java
public class Main {
    private static final String SENTENCE = "채채채 상상상 엽엽엽";

    public static void main(String[] args) {
        Stream<Character> stream = IntStream.range(0, SENTENCE.length())
                                            .mapToObj(SENTENCE::charAt);
        System.out.println(countWords(stream));
    }

    private static int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulator,
                WordCounter::combine);

        return wordCounter.getCounter();
    }
}

```

```
3
```

스트림을 탐색하면서 새로운 문자를 찾을 때마다 `accumulate()` 메서드를 호출한다. `countWordsIteratively()`에서와 같이 새로운 비공백 문자를 탐색하고 마지막 문자가 공백이면 `counter`를 증가시킨다. 그리고 `combine()`은 `WordCounter` 내부의 `counter` 값을 서로 합치는 역할을 한다.

**WordCounter 병렬로 수행하기**

아래 처럼 병렬 스트림으로 처리하려고 하면 원하는 답이 나오지 않는다.

```java
System.out.println(countWords(stream.parallel()));
```

그 이유는 원래 문자열의 임의의 위치에서 둘로 분할하다 보니, 예상치 못하게 하나의 단어를 둘로 나누어서 계산하는 경우가 발생할 수 있기 때문이다. 이를 해결하기 위해서는 문자열의 임의의 위치에서 분할하는 것이 아닌, 단어가 끝나는 위치에서만 분할이 이루어지도록 함으로써 문제를 해결할 수 있다.

```java
public class WordCounterSpliterator implements Spliterator<Character> {
    private final String string;
    private int currentChar = 0;

    public WordCounterSpliterator(String string) {
        this.string = string;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++)); // 현재 문자 소비
        return currentChar < string.length(); // 소비할 문자가 남아 있으면 true 반환
    }

    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;
        if (currentSize < 10) {
            return null; // 파싱할 문자열이 순차 처리할 수 있을 만큼 충분히 작아졌으면 null 반환
        }
        // 파싱할 문자열의 중간을 분할 위치로 설정
        for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
            // 다음 공백이 나올떄까지 분할 위치를 뒤로 이동시킨다
            if (Character.isWhitespace(string.charAt(splitPos))) {
                // 처음부터 분할 위치까지 문자열을 파싱할 새로운 WordCounterSpliterator 생성
                Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
                currentChar = splitPos; // WordCounterSpliterator의 시작위치를 분할위치로 설정
                return spliterator; // 공백 찾고 문자열 분리 했으므로 반복 종료
            }
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
```

