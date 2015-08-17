import java.io.*;
import java.util.Scanner;

public class tt1130908
{
	public static void main(String[] args) throws IOException
	{
		Scanner s = new Scanner(System.in);
		System.out.print("Enter file name : ");
		String fileName = s.next();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		int N = Integer.parseInt(br.readLine());
		int n = Integer.parseInt(br.readLine());
		graph myGraph = new graph(N,n);
		for (int i = 0; i < n; i++) 
		{
			line = br.readLine();
			String[] a = line.split(" ");
			myGraph.add(Integer.parseInt(a[0]),Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]), Integer.parseInt(a[5]),a[4]);   
		}
		int numOfTestCases = Integer.parseInt(br.readLine());
		for (int i = 0; i < numOfTestCases; i++) 
		{
			line = br.readLine();
			String[] a = line.split(" ");
			System.out.println(myGraph.dijkstra(Integer.parseInt(a[0]),Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3])));
		}
	} 
}

class neighbourNode
{
    int cityNo;
    int price;
    int arrTime;
    int depTime;
    String flightNo;
    neighbourNode next;
    neighbourNode(int c,int p,int d,int a,String s)
    {
        flightNo = s;
        cityNo = c;
        price = p;
        arrTime = a;
        depTime = d;
        next = null;
    }
}
class graphNode
{
    int cityNo;
    double weightage;
    int arrTime;
    neighbourList neighbourList;
    graphNode(int c)
    {
        cityNo = c;
        neighbourList = new neighbourList();
    }
}

class flightNode 
{
    String flightNo;
    double weightage;
    int arrCity;
    int arrTime;
    flightNode(String f,int c,int a)
    {
        arrTime = a;
        arrCity = c;
        flightNo = f;
        weightage = Math.tan(Math.PI/2);
    }
    flightNode()
    {
        weightage = Math.tan(Math.PI/2);
    }
}
class neighbourList 
{
    neighbourNode lastNode;
    neighbourNode firstNode;
    neighbourList()
    {
        lastNode = null;
        firstNode = null;
    }
    void add(int c,int p,int d,int a,String s)
    {
        if(firstNode == null)
        {
            firstNode = new neighbourNode(c,p,d,a,s);
            lastNode = firstNode;
        }
        else 
        {
            lastNode.next = new neighbourNode(c,p,d,a,s);
            lastNode = lastNode.next;
        }
    }
}
class graph 
{
    graphNode[] array;
    int size;
    int numFlights;
    flightNode[] array1;
    graph(int N,int n)
    {
        size = N;
        numFlights = n;
        array = new graphNode[N];
        array1 = new flightNode[numFlights];
        for(int i = 1;i <= N;i++)
            array[i-1] = new graphNode(i);
    }
    int value(String s)
    {
        int x = 0;
        for(int i = 0; i < s.length();i++)
        {
            x += s.charAt(i);
        }
        return x%numFlights;
    }
    void add(flightNode[] f,String s,int c,int a)
    {
        int init = value(s);
        int i = init;
        int j = 1;
        while(f[i]!=null)
        {
            i = (init+j)%numFlights;
            j += 1;
        }
        f[i] = new flightNode(s,c,a);
    }
    int find(String s)
    {
        int init = value(s);
        int i = init;
        int j = 1;
        while(array1[i].flightNo != s)
        {
            i = (init+j)%numFlights;
            j += 1;
        }
        return i;
    }
    void add(int c1,int c2,int d,int a,int p,String flightNo)
    {
        array[c1-1].neighbourList.add(c2,p,d,a,flightNo);
        add(array1,flightNo,c2,a);
    }
    int getMin(flightNode[] a)
    {
        int min = 0;
        for(int i = 0; i < a.length; i++)
        {
            if(a[i].weightage != -Math.tan(Math.PI/2))
            {
                min = i;
                break;
            }
        }
        for(int i = 0; i < a.length; i++)
            if(a[i].weightage != -Math.tan(Math.PI/2) && a[i].weightage < a[min].weightage) min = i;
        return min;
    }
    boolean over()
    {
        for (int i = 0; i < array1.length; i++) 
        {
            if(array1[i].weightage!=Math.tan(Math.PI/2) && array1[i].weightage!=-Math.tan(Math.PI/2)) 
                return false;
        }
        return true;
    }
    int add30(int h)
    {
        int n = h + 30;
        if(n%100 >= 60)
            n = n + 100 - (n%100) + (n%100)%60;
        return n;
    }
    int dijkstra(int source,int dest,int lowerLim,int upperLim)
    {
        for (int i = 0; i < array.length; i++) 
            array[i].weightage = Math.tan(Math.PI/2);
        for (int i = 0; i < array1.length; i++) 
            array1[i].weightage = Math.tan(Math.PI/2);
        array[source - 1].weightage = 0;
        neighbourNode c = array[source - 1].neighbourList.firstNode;
        while(c != null)
        {
            double d1 = array[c.cityNo - 1].weightage;
            double d2 = c.price;
            if(c.depTime >= lowerLim && c.arrTime <= upperLim)
            {
                array1[find(c.flightNo)].weightage = c.price;
                if(d1 > d2)array[c.cityNo - 1].weightage = c.price;
            }
            c = c.next;
        }
        while(!over())
        {
            int m = getMin(array1);
            neighbourNode current = array[array1[m].arrCity - 1].neighbourList.firstNode;
            while(current!=null)
            {
                double d1 = array[current.cityNo - 1].weightage;
                double d2 = array1[m].weightage + current.price;
                if(d1 > d2 && current.depTime >= add30(array1[m].arrTime) && current.arrTime <= upperLim)
                {
                    array1[find(current.flightNo)].weightage = d2;
                    array[current.cityNo - 1].weightage = d2;
                }
                current = current.next;
            }
            array1[m].weightage = -Math.tan(Math.PI/2);            
            if(array1[getMin(array1)].arrCity == dest) break;
        }
        if(array[dest - 1].weightage == Math.tan(Math.PI/2)) array[dest - 1].weightage = -1;
        return ((int)array[dest - 1].weightage);
    }
}
