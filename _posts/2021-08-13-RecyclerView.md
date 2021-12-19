# RecyclerView

- #### **리사이클러뷰란?**

    - 이름 그대로 각각의 요소들을 재활용한다

        - 화면이 스크롤 될 때, 리사이클러뷰는 뷰를 파괴하는 대신 뷰를 재활용하고 스크롤을 함으로써 나타나는 새로운 아이템을 뷰에 담는다.
        - 이러한 재활용은 앱을 반응속도, 전력소모 감소, 퍼포먼스 향상에 영향을 미친다.

        

- #### 핵심 클래스들

  - 리사이클러뷰는 여러개의 뷰를 담는 **ViewGroup**이다.
  - 각각의 리스트의 개별적 요소들은 뷰 홀더 객체로 정의된다.
    **ViewHolder**가 생성될때 는 뷰 홀더에 연결된 데이터가 없다. 
    뷰 홀더가 생성된 후 **RecyclerView**가 **ViewHolder**를 뷰의 데이터에 바인딩 한다. 
    **ViewHolder**를 정의하기 위해서는 **RecyclerView.ViewHolder**를 상속 받아 정의한다.
  - 리사이클러뷰는 뷰를 요청한 다음, 어댑터에서 메소드를 호출하여 뷰를 뷰의 데이터에 바인딩한다.
    **RecyclerView.Adapter**를 확장하여 어댑터를 정의할 수 있다.
  - **LayoutManager**는 리스트의 개별적 요소들을 배열시킨다. 리사이클러뷰 라이브러리에서 제공하는 레이아웃을 사용하거나, 커스텀하여 정의할 수 있다. 레이아웃 매니저는 **LayoutManager** 추상 클래스 기반이다.



- #### 리사이클러뷰 사용법

  1. 어떤 레이아웃을 사용할 것인지 결정 ( list , grid ... )
  2. 각 요소들이 리스트에서 어떻게 보여지고, 동작할지 설계
     - **ViewHolder** 클래스를 기반으로 확장한다.
       - 사용중인 ViewHolder 버전은 리스트 항목에 필요한 모든 기능을 제공한다.
       - ViewHolder는 View의 래퍼이고 그 뷰는 RecyclerView로 관리된다.
  3. 데이터를 **ViewHolder**를 뷰와 연결하는 **Adapter**를 정의한다.



- #### 어댑터 및 뷰 홀더 구현

  - 레이아웃을 결정한 이후 **Adapter** 및 **ViewHolder**를 구현해야한다.

  - \** <span style="color:red">**바인딩**</span> : 뷰를 데이터에 연결하는 프로세스

  - **onCreateViewHolder()**

    - ViewHolder를 새로 만들어야 할 때마다 호출되는 메소드
    - ViewHolder와 그에 연결된 View를 생성하고 초기화하지만 뷰의 내용을 채우지는 않는다. 뷰 홀더가 특정 데이터에 바인딩 된 상태가 아니기 때문
    - View 객체를 담고있는, ViewHolder를 생성한다.  ViewHoler는 레이아웃을 인자로 받아서 기억하고 있는데, 쉽게 말해서 레이아웃을 만들어주는 역할을 한다고 이해하면 될 것 같다

  - **onBindViewHolder()**

    - 데이터와 관련이 있을때 호출한다.
    - 적절한 데이터를 가져오고 ViewHolder의 레이아웃을 채우는데 사용한다.
      - ex) 리사이클러뷰가 이름 목록을 보여주고 있다면, 이 메소드는 아마 목록에서 적절한 이름을 찾아 ViewHolder의 **TextView**에 채울 것이다.

  - **getItemCount()**

    - 리사이클러뷰 데이터 집합의 크기를 구할때 호출한다
    - 보통 ArrayList를 만들어서 아래와 같이 사용한다.

    ```java
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<Item> items = new ArrayList<Item>();
      
        // ViewHolder 클래스 정의
        // findViewById를 보통 정의 함 
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                textView = (TextView) view.findViewById(R.id.textView);
            }
    
            public TextView getTextView() {
                return textView;
            }
        }
    
       	// 어댑터의 데이터셋 초기화
        public CustomAdapter(ArrayList<Item> items) {
            this.items = items;
        }
    
        // 뷰를 생성한다.
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.text_row_item, viewGroup, false);
            return new ViewHolder(view);
        }
    
        // 뷰의 내용을 바꿈
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            // 이 위치에서 데이터셋으로부터 요소를 가져오고 뷰의 내용을 해당 요소로 바꾼다.
            viewHolder.getTextView().setText(items);
        }
    
        // 데이터셋의 사이즈를 반환한다.
        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    ```