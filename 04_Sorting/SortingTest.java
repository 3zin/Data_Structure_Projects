import java.io.*;
import java.util.*;
 
public class SortingTest
{
    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
        try
        {
            boolean isRandom = false;   // 입력받은 배열이 난수인가 아닌가?
            int[] value;    // 입력 받을 숫자들의 배열
            String nums = br.readLine();    // 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r')
            {
                // 난수일 경우
                isRandom = true;    // 난수임을 표시
 
                String[] nums_arg = nums.split(" ");
 
                int numsize = Integer.parseInt(nums_arg[1]);    // 총 갯수
                int rminimum = Integer.parseInt(nums_arg[2]);   // 최소값
                int rmaximum = Integer.parseInt(nums_arg[3]);   // 최대값
 
                Random rand = new Random(); // 난수 인스턴스를 생성한다.
 
                value = new int[numsize];   // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)   // 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            }
            else
            {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);
 
                value = new int[numsize];   // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)   // 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }
 
            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true)
            {
                int[] newvalue = (int[])value.clone();  // 원래 값의 보호를 위해 복사본을 생성한다.
 
                String command = br.readLine();
 
                long t = System.currentTimeMillis();
                switch (command.charAt(0))
                {
                    case 'B':   // Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':   // Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':   // Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':   // Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':   // Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':   // Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'X':
                        return; // 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom)
                {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                }
                else
                {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++)
                    {
                        System.out.println(newvalue[i]);
                    }
                }
 
            }
        }
        catch (IOException e)
        {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }
 
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Bubble Sort
     * 외부 자료를 참고하지 않고 원래 알고 있는 알고리즘을 바탕으로 구현함.
     * 리스트를 순회하며 인접한 두 원소의 대소를 비교해 큰 원소를 계속해서 오른쪽으로 옮기는 방식을 사용한다.
     * token을 통해 더이상 교환이 이루어지지 않으면 빠져나오게 최적화 해보려 했지만 오히려 overhead를 발생시켰다. 이는 각주로 남겨두었다.  
     */
    
    private static int[] DoBubbleSort(int[] value)
    { 	

    	//boolean token;
    	
    	for(int i=0 ; i<value.length ; i++){
    	//	token = false;
    		for(int j=0 ; j<value.length-i-1 ; j++){
    			if(value[j] > value[j+1]){
    				int tmp = value[j];
    				value[j] = value[j+1];
    				value[j+1] = tmp;
    	//			token = true;
    			}
    		}
    	//	if(!token)
    	//		break;
    	}
        return (value);
    }
 
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Insertion Sort
     * copy from http://marobiana.tistory.com/85 (가장 대표적인 구현 방법인지라 저작권은 원작자가 따로 제한하고 있지 않았으며, 원작자에게 양해를 구하고 가져옴) 
     * 리스트를 순회하며 마주치는 원소를 왼쪽 정렬된 부분 내부의 알맞은 장소에 삽입하는 방식을 사용한다. 
     */
    
    private static int[] DoInsertionSort(int[] value)
    {
    	
    	for(int i=1 ; i<value.length ; i++){
    		int base = value[i]; //비교의 기준이 되는 값 (삽입될 값) 
    		int index = i-1;     //비교가 시작될 index 
    		
    		
    		// 기준 원소 뒤로 순회하며 정렬된 배열에 값을 삽입할 자리를 만들어준다. 
    		while(index >=0 && value[index] > base){
    			value[index+1] = value[index];
    			index--;
    		}
    		// 알맞은 자리에 값을 삽입한다. 
    		value[index+1] = base;
    	}
        return (value);
    }
 
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Heap Sort
     * modification from 문병로, <쉽게 배우는 알고리즘>, 한빛아카데미, 2014, pg.95,99
     * pseudo code만 참고했으며, 구체적인 구현은 본인이 함. 
     * 
     * 최대힙을 사용하였으며, (1) 트리 배열을 힙의 성질을 만족하는 힙 배열로 만드는 과정 (2) 배열에서 한 원소씩 제외하며 소팅하는 과정으로 이루어져 있다.
     */
    
    private static int[] DoHeapSort(int[] value)
    {
        
    	// 트리 배열을 힙 배열로 만든다 
    	// 최초의 internal 노드부터 시작한다. (value[value.length/2])
    	for (int i = value.length/2 ; i>=0 ; i--){
    		percolateDown(value, i, value.length-1);
    	}
    	
    	// 힙 배열에서 한 원소씩 제외하며 소팅한다. 
    	// 최대힙이므로 value[0]이 최대값이다. 
    	for (int i = value.length-1 ; i>=1 ; i--){
    		
    		int tmp = value[0];
    		value[0] = value[i];
    		value[i] = tmp;
    		
    		percolateDown(value, 0, i-1);
    	}
        return (value);
    }
    
    
    // value[start]를 루트로 하는 트리
    // last는 트리의 마지막 인덱스이다
    private static void percolateDown(int[] value, int start, int last){
    	
    	int left = 2*start;
    	int right = 2*start+1;
    	int max;
    	
    	// 두 노드 모두 있는 경우
    	if(right<=last){
    		if(value[left] < value[right]){
    			max = right;
    		}else{
    			max = left;
    		}
    	//왼쪽 노드만 있는 경우 
    	}else if(left<=last){
    		max = left;
    	//리프 노드일 경우
    	}else{
    		return;
    	}
    	
    	if(value[max] > value[start]){
    		
    		int tmp = value[max];
    		value[max] = value[start];
    		value[start] = tmp;
    		
    		percolateDown(value, max, last);
    	}
    	
    }
 
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Merge Sort
     * modification from Janet J.Prichard, <Data Abstraction & Problem Solving with Java, third Edition>, Pearson,2011, pg.552-555
     * pseudo code와 알고리즘만 참고했으며, 구체적인 구현은 본인이 함. 
     * 
     * 리스트를 원소의 개수가 한 개가 될 때까지 절반씩 나누고(mergeSort()), 나누어진 리스트를 두 개씩 짝을 지어 재귀적으로 병합하는 방법으로(merge()) 전체 리스트를 정렬한다.
     * input 리스트와 같은 크기의 tmp 리스트를 미리 만들어 놓고 레퍼런스를 지속적으로 전달하는 방식을 사용했다. 
     */
    
    private static int[] DoMergeSort(int[] value)
    {
        
    	// 병합 중간에 사용할 input과 같은 크기의 tmp list
    	int[] tmp = new int[value.length];
    	mergeSort(value, tmp, 0, value.length-1); 
        return (value);
    }
    
    private static void mergeSort(int[] value, int[] tmp, int l, int r)
    {
    	if(l<r){
    		int m = (l+r)/2;
    		mergeSort(value, tmp, l, m); // [left ~ mid]
    		mergeSort(value, tmp, m+1, r); // [mid+1 ~ right]
    		merge(value, tmp, l, m, r); // 분할한 두 리스트 병합 
    	}
    }
    
    private static void merge(int[] value, int[] tmp, int l , int m, int r)
    {
    	
    	int first_l = l;
    	int first_r = m; // [left ~ mid]
    	
    	int second_l = m+1;
    	int second_r = r; // [mid+1 ~ right]
    	
    	int index = l; // 마지막 과정에서 value[]에 복사할 때 사용할 인덱스 
    	
    	while(first_l <= first_r && second_l <= second_r){
    		if(value[first_l] < value[second_l]){
    			tmp[index++] = value[first_l++];
    		}else{
    			tmp[index++] = value[second_l++];
    		}
    	}
    	
    	while(first_l <= first_r){ // Optional leftovers
    		tmp[index++] = value[first_l++];
    	}
    	
    	while(second_l <= second_r){ // Optional leftovers
    		tmp[index++] = value[second_l++];
    	}
    	
    	for(int i=l ; i<=r ; i++){ //Copy from tmp[] to value[]
    		value[i] = tmp[i];
    	}   	
    }
    
 
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Quick Sort
     * modification from Janet J.Prichard, <Data Abstraction & Problem Solving with Java, third Edition>, Pearson,2011, pg.557-567
     * pseudo code와 알고리즘만 참고했으며, 구체적인 구현은 본인이 함. 
     * 
     * 임의로 선택한 pivot을 기준으로 리스트를 pivot보다 작은 구역, 같거나 큰 구역으로 구분하며, 각각의 구역에 대해 또 pivot을 설정하며 재귀적으로 소팅한다. 
     * input data의 범위가 좁을 경우 Stack OverFlow가 발생할 수 있다. 이 부분에 대해서는 게시판에 공지된 대로 따로 처리하지 않았다. 
     */
    
    private static int[] DoQuickSort(int[] value)
    { 
    	
    	quickSort(value, 0, value.length-1);
        return (value);
    }
    
    private static void quickSort(int[] value, int l, int r){
    	
    	int pivotIndex; 
    	
    	if(l<r){
    		pivotIndex = partition(value, l, r);
    		quickSort(value, l, pivotIndex-1);
    		quickSort(value, pivotIndex+1, r);
    	}
    }
    
    
    private static int partition(int[] value, int l, int r){
    	
    	int pivot = value[r]; // 맨 마지막 값을 pivot으로 설정한다
    	
    	int firstEnd = l; // 첫 번째 구역의 마지막 index + 1
    	int secondEnd; // 두 번째 구역의 마지막 index + 1
    	
    	// 만약 마주치는 값이 pivot보다 작을 경우(1구역에 속할 경우) firstEnd의 자리에 넣고 첫 번째 구역을 한 칸 증가시킨다. 
    	// firstEnd의 값은 곧 첫 번째 구역의 마지막 index + 1을 가리키며, 이는 secondStart의 값을 넘을 수 없다.
    	
    	for(secondEnd = l ; secondEnd < r ; secondEnd++){ 
    		if(value[secondEnd] <= pivot){
    			int tmp1 = value[firstEnd];
    			value[firstEnd] = value[secondEnd];
    			value[secondEnd] = tmp1;
    			firstEnd++;
    		}
    	}
    	
    	// 첫 번째 구역의 마지막 자리에 pivot을 넣고 pivot의 자리를 리턴한다.
    	value[r] = value[firstEnd];
    	value[firstEnd] = pivot;
    	return firstEnd;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Radix Sort
     * modification from http://palpit.tistory.com/129 (저작권은 원작자가 따로 제한하고 있지 않았으며, 원작자에게 양해를 구하고 가져옴) 
     * LinkedList를 사용하는 알고리즘과 자릿수를 얻어내는 방법 등을 차용했으며, 구체적인 구현이나 음수 처리 방법 등은 본인이 생각해서 함.
     * 
     * LinkedList(Queue/Stack)을 사용해서 직관적으로 구현하였다. 음의 정수에 대해서도 처리할 수 있도록 구현하였다.
     * 최대 자릿수를 찾기 위한 한 번의 순회 + (최대 자릿수+1) * (리스트와 Queue 순회하며 offer/poll)로 O(kn)의 복잡도를 만족한다.  
     */
    
    private static int[] DoRadixSort(int[] value)
    {

    	if(value.length==0){
    		return value;//Exception.
    	}
    	
    	int maxDigit = value[0]; //최댓값

    	// 배열을 순회하며 최대 자릿수를 얻어낸다. 
    	// 음수 input을 고려해서 절댓값을 기준으로 최댓값을 구한 후, 로그 연산을 통해 자릿수를 얻어낸다. 
    	for(int i = 0 ; i < value.length ; i++){
    		if(Math.abs(value[i]) > maxDigit){
    			maxDigit = Math.abs(value[i]);
    		}
    	}
    	
    	radixSort(value, (int) Math.log10(maxDigit)+1);
        return (value);
    }
    
    
    // 정렬할 배열과 최대 자릿수를 매개변수로 전달받는다.  
    private static void radixSort(int[] value, int digit){
    	
    	// 자릿수별 저장을 위해 LinkedList를 Queue 형태로 사용한다(배열의 인덱스 = digit)
    	@SuppressWarnings("unchecked")
		LinkedList<Integer>[] digitNums = new LinkedList[] {new LinkedList<Integer>(), new LinkedList<Integer>(), new LinkedList<Integer>(), 
    														new LinkedList<Integer>(), new LinkedList<Integer>(), new LinkedList<Integer>(),
    														new LinkedList<Integer>(), new LinkedList<Integer>(), new LinkedList<Integer>(),
    														new LinkedList<Integer>()};
    	

    	// (num%std)/dvd 계산으로 digit을 얻어낼 수 있음 
    	int std = 10;
    	int dvd = 1;
    	
    	
    	// 배열을 최대 자릿수만큼 순회하며 LSB부터 한 digit씩 stable한 기수 정렬을 시작한다. 
    	
    	for(int i = 0 ; i<digit ; i++, std*=10, dvd*=10){
    		
    		
    		// 마주치는 원소의 특정 자릿수를 얻어내어 배열의 해당 인덱스 Queue에 순차적으로 offer하여 stable하게 정렬한다.
        	// 음수의 경우 양수로 생각해서 계산한다. 
    		for(int j = 0; j<value.length ; j++){
    			
    			int tmpNum;
    			
    			if(value[j]<0){
    				tmpNum = (Math.abs(value[j]) % std) / dvd;
    			}else{
    				tmpNum = (value[j] % std) / dvd;
    			}
    			digitNums[tmpNum].offer(value[j]);
    		}
    		
    		// 자릿수별로 정렬된 Queue 배열에서 원래의 value 배열로 다시 값을 옮겨와 갱신한다. 
    		// Queue의 앞에서부터 Queue가 빌 때까지 순차적으로 하나씩 poll하기 때문에 Stable Sort가 보장된다.  
    		int tmp = 0;
    		for(int j = 0; j<digitNums.length ; j++){
    			while(!digitNums[j].isEmpty()){
    				value[tmp++] = digitNums[j].poll();
    			}
    		}
    	}
    	
    	// 전체 리스트를 다시 한 번 순회하며 음수를 처리한다. 
    	// 부호값 +- 역시 ‘기수’의 일종이 아닐까 하는 생각에서 착안하여, 음수를 digitNums[0]에, 양수를digitNums[1]에 할당하고 기수정렬을 한 번 더 실행한다.  
    	// 단, 음수의 경우 양수와 달리 절대값이 큰 값이 더 작은 값이므로 queue대신 stack 자료구조를 사용해 offer/poll 연산을 push/pop 연산으로 대체하였다.
    	for(int i = 0; i<value.length ; i++){
    		if(value[i]<0){
    			digitNums[0].push(value[i]);
    		}else{
    			digitNums[1].offer(value[i]);
    		}
    	}
    	
    	int tmp = 0;
    	
    	while(!digitNums[0].isEmpty()){
    		value[tmp++] = digitNums[0].pop();
    	}
    	while(!digitNums[1].isEmpty()){
    		value[tmp++] = digitNums[1].poll();
    	}
    	
    }
}
