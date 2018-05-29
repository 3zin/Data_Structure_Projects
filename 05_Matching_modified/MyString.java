

/**********************************************************************************************************
 * Class MyString 
 *
 * String에 대한 Custom HashCode Function을 구현하기 위해 제작한 별도의 클래스.
 *
 * Java에서 String Class는 final로 저장되어 있으므로 직접 상속받는 것이 불가능하다. 
 * String에 대한 hashCode() 메소드를 원하는 방식대로 구현하기 위해서는 String 변수를 내부적으로 포함하는 새로운 class type을 생성할 필요가 있다. 
 *
 */
public class MyString implements Comparable<MyString> {
	
private String str;
	
	MyString(String str){
		this.str = str;
	}
	
	/**
	 * hashCode function
	 * 
	 * hash function: (k character들의 ASCII code들의 합) mod 100
	 */
	@Override
	public int hashCode(){
		
		int sum = 0;
		for(int i = 0 ; i< str.length(); i++){
			sum += (int) str.charAt(i);
		}
		return sum % 100;
	}
	
	@Override
	public String toString(){
		return this.str;
	}

	@Override
	public int compareTo(MyString o) {
		return str.compareTo(o.toString());
	}

}
