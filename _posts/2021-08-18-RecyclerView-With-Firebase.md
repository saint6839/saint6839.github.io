# 파이어베이스 리사이클러뷰 갱신

- <span style="color:red"> **RecyclerView with Firebase DB**</span>
    
    1. 파이어베이스에서 제공하는 메소드 중 **addValueEventListener()**를 사용
        1. 이 안에서 onDataChange() 메소드를 재정의한다
    
            → DataSnapShot을 통해 DB의 변화를 감지하고, 그에 따른 이벤트를 지정해 줄 수 있다.
    
        TodoWriteActivtiy.class
    
        ![Untitled](../image/Untitled16.png)
    
        **⇒ onClick()메소드를 통해서 Todo의 내용을 DB에 추가한 뒤**
    
        ```java
        DatabaseReference myRef;
        myRef = database.getReference()
        									.child("Todos")
        									.child(groupKey)
        									.child((month+1)+"월")
        									.child(dayOfMonth+"일");
        						
        ```
    
        ⇒ 다음 참조의 값 변화를 아래의 메소드에서 감지한다.
    
        ```java
        myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        items.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Todo todo = dataSnapshot.getValue(Todo.class);
                            items.add(todo);
                            customAdapter.setItems(items);
                        }
                        customAdapter.notifyDataSetChanged();
                    }
        
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Log.e("TodoWriteActivity: ", String.valueOf(error.toException()));
                    }
                });
        ```
    
        **⇒ Todo todo = dataSnapshot.getValue(Todo.class)** 
    
        1. myRef밑의 값(Todo 데이터 변수 값)을 추출해서 
        2. Todo 객체변수 todo로 저장하고
        3. ArrayList객체인 items에 add.
        4. 어댑터에 ArrayList객체 items를 통째로 넘김
        5. notifyDataSetChanged() 호출하여 리사이클러뷰 갱신 
    
        <span style="color:red">**즉 DB값의 변화를 바탕으로 어댑터를 갱신 시키는 것이다**</span>
    
- push()로 넣은 난수 값 하위 항목 삭제하는법 ⇒ 참조 변수 선언시에 .push()를 포함시킨뒤, 참조변수에 getKey()를 하면 됨

