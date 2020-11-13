package com.atguigu.pojo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车对象
 */
public class Cart {
    private Map<Integer,CartItem> items = new LinkedHashMap<Integer,CartItem>();


    /**
     * @MethodName: 添加商品项
     * @Return:
     **/
    public void addItem(CartItem cartItem){
        // 先查看购物车中是否已经添加过此商品， 如果已添加， 则数量累加， 总金额更新， 如果没有添加过， 直接放到集合中即可
        CartItem item = items.get(cartItem.getId());

        if(item == null){
            // 之前没添加过此商品
            items.put(cartItem.getId(), cartItem);
        }else{
            // 已经 添加过的情况
            item.setCount(item.getCount() + 1);  // 数量累加
            item.setTotalPrice(item.getPrice().multiply(new BigDecimal(item.getCount())));  //更新总金额

        }
    }

    /**
     * @MethodName: 删除商品项
     * @Return:
     **/
    public void deleteItem(Integer id){
        items.remove(id);
    }

    /**
     * @MethodName: 清空购物车
     * @Return:
     **/
    public void clear(){
        items.clear();
    }

    /**
     * @MethodName: 修改商品数量
     * @Return:
     **/
    public void updateCount(Integer id,Integer count){

        CartItem cartItem = items.get(id);

        if(cartItem != null){
            // 不为空代表购物车中有这个商品，把这个商品的数量更改为传入的参数
            cartItem.setCount(count);
            //更新总金额
            cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount())));
        }

    }




    public Integer getTotalCount() {
        //我们通过每次调用获取totalCount方法是，
        Integer totalCount = 0;
        for(Map.Entry<Integer,CartItem> entry : items.entrySet()){
            totalCount += entry.getValue().getCount();
        }
        return totalCount;
    }



    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for(Map.Entry<Integer,CartItem> entry : items.entrySet()){
            totalPrice =totalPrice.add(entry.getValue().getTotalPrice());
        }

        return totalPrice;
    }



    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, CartItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "totalCount=" + getTotalCount() +
                ", totalPrice=" + getTotalPrice() +
                ", items=" + items +
                '}';
    }
}
