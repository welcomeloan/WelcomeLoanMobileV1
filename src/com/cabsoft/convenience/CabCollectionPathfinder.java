package com.cabsoft.convenience;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/***
 * collection 객체의 탐색을 보다 쉽게 해주는 class
 * 사용 방법은 다음과 같다.
 * 
 * <pre>
 * 
 * // collection pathfinder를 사용한 경우.
 * 
 * CabCollectionPathfinder finder = new CabCollectionPathfinder();
 * 
 * finder.setCollection(somethingCollection);
 * 
 * Object obj = finder.getObject(1, "family", 0, "name");
 * 
 * // collection pathfinder를 사용하지 않는 경우.
 * 
 * Map {@code<}String, Object> map = (Map {@code<}String, Object>) somethingCollection.get(1);
 * List {@code<}Map{@code<}String, Object>> list = (List {@code<}Map{@code<}String, Object>>) map.get("family");
 * map = list.get(0);
 * Object obj1 = map.get("name");
 * 
 * </pre>
 * 
 * @author kangseungchul
 *
 */
public class CabCollectionPathfinder {
	Object mCollection;
	
	static public Object getObjectForCol(Object collection, Object... keys)
	{
		CabCollectionPathfinder finder = new CabCollectionPathfinder(collection);
		return finder.getObject(keys);
	}
	
	public CabCollectionPathfinder(){

	}
	
	public CabCollectionPathfinder(Object collection){
		this.mCollection = collection;
	}
	
	public void setCollection(Object collection){
		this.mCollection = collection;
	}
	
	
	
	public Object getObject(Object... keys)
	{
		return this.getObject(this.mCollection, keys);
	}
	
	private Object getObject(Object collection, Object... keys){
		Object value = null;
		
		try {
			Method method = null; 
			
			//현재 키값을 뽑아내기 위해 ArrayList로 변환하고 사용된 키값을 제거합니다.
			ArrayList<Object> keyArray = new ArrayList<Object>(Arrays.asList(keys));
			Object key = keyArray.get(0);
			keyArray.remove(0);

			method = findGetObjectKeyMethod(collection);
			if(method != null){
				value = (Object)method.invoke(collection, key);
				if(value == null || keys.length == 1) return value;
				return this.getObject(value, keyArray.toArray());
			}

			method = findGetIntKeyMethod(collection);
			if(method != null){
				value = (Object)method.invoke(collection, ((Integer)key).intValue());
				if(value == null || keys.length == 1) return value;
				return this.getObject(value, keyArray.toArray());
			}
			
			//그 외에 추적이 불가능 할 시에는 null 반환.
			return null;
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private Method findGetObjectKeyMethod(Object collection)
	{
		//get(Object)형태.(Map, HashMap에 사용됩니다.)
		try {
			return collection.getClass().getMethod("get", Object.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Method findGetIntKeyMethod(Object collection)
	{
		//get(int)형태.(Array, ArrayList에 사용됩니다.)
		try {
			return collection.getClass().getMethod("get", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}
}
