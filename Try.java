//Project by Surabhi Jain
//jain.281
//How to compile and run this file
//javac Try.java
//java Try 0<data1.txt 1>output.txt


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Timee {//To print all the values assuming Restaurant starts at time 0
	static long present;
	
	Timee(long n)
	{
		present = n;
	}
	
	Timee()
	{
		
	}
	
	public static void print_Cust_Entered(int i,long t)
	{
		System.out.println("Customer "+i+" entered at "+((t-present)/10));
	}
	
	
	public static void print_Cus_got_Chef(int customer_id, int i,long t)
	{
		System.out.println("Customer "+customer_id+" got chef number "+ i+" at "+((t-present)/10));
	    
	}

	public static void cust_start_eat(int customer_id, long t) {
		System.out.println("Customer "+customer_id+" started eating at "+((t-present)/10));
		
	}

	public static void cust_left_table(int customer_id, long t) {
		System.out.println("Customer "+customer_id+" left table at "+((t-present)/10));
		
	}

	public static void cust_got_mA(int customer_id, long t) {
		System.out.println("Customer "+customer_id+" got machine A at "+((t-present)/10));
		
	}
	
	public static void cust_got_mB(int customer_id, long t) {
		System.out.println("Customer "+customer_id+" got machine B at "+((t-present)/10));
		
	}
	public static void cust_got_mC(int customer_id, long t) {
		System.out.println("Customer "+customer_id+" got machine C at "+((t-present)/10));
		
	}

	public static void cust_got_table(int customer_id, int i,long t) {
		System.out.println("Customer "+customer_id+" got table number "+ i+" at "+((t-present)/10));
		
	}
}

class Table extends Thread
{
	int arr[];
	int id;
	int customer_id;
	int totalnumber;
	
	Table(int n)
	{
		totalnumber=n;
		arr = new int[n];
		for(int i=0;i<n;i++)
			arr[i]=0;
	}
	
	boolean allBooked() //check if all the tables are occupied
	{
		int i;
		for(i=0;i<totalnumber;i++)
		{
			if(arr[i]==0)
				return false;
		}
		
		return true;
	}
	
	synchronized void booktable(Customer c)
	{
		
		try{
			while(allBooked())
			{
				this.wait();
			}
			}catch(Exception e)
			{
				
			}

		
		int i;
		for(i=0;i<totalnumber;i++)
		{
			if(arr[i]==0)
			{
				c.table_no=i;
				arr[i]=1;
				
				Timee.cust_got_table(c.customer_id,i,System.currentTimeMillis());
				
				break;
			}
		}
		
				
	}
	
	synchronized void leavetable(Customer c) //customer leaves the table and waiting customers get notified
	{
		arr[c.table_no]=0;
		this.notify();
		//should notify other customer
	}
}

class Order
{
	int id;
	int bur;
	int fry;
	int coke;
	
	public Order(int a,int b,int c,int d)
	{
		id=a;
		bur=b;
		fry=c;
		coke=d;
	}
}

class MachineC
{
	int customer_id;
	static int ready1;
	
	public MachineC()
	{
		customer_id=-1;
		ready1=1;
	}
	
	synchronized void getmachineC(Customer c,int i)
	{
		customer_id=c.customer_id;
		while(ready1!=1)
		{
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ready1=0;
		
		Timee.cust_got_mC(c.customer_id,System.currentTimeMillis());
		try {
			Thread.sleep(10*i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	synchronized void leavemachineC(Customer c)
	{
		ready1=1;
		this.notify();
	}
	
	
}

class MachineB
{
	int customer_id;
	int ready1;
	
	public MachineB()
	{
		customer_id=-1;
		ready1=1;
	}
	
	synchronized void getmachineB(Customer c,int i)
	{
		customer_id=c.customer_id;
	
		while(ready1!=1)
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		Timee.cust_got_mB(c.customer_id,System.currentTimeMillis());
		ready1=0;
		try {
			Thread.sleep(30*i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	synchronized void leavemachineB(Customer c)
	{
		ready1=1;
		this.notify();
	}
		
}

class MachineA
{
	int customer_id;
	static int ready1;
	
	public MachineA()
	{
		customer_id=-1;
		ready1=1;
	}
	
	synchronized void getmachineA(Customer c,int i)
	{
		customer_id=c.customer_id;
		while(ready1!=1)
		{
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ready1=0;
		
		Timee.cust_got_mA(c.customer_id,System.currentTimeMillis());
		try {
			Thread.sleep(50*i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	synchronized void leavemachineA(Customer c)
	{
		ready1=1;
		this.notify();
	}
	
	
}

class Customer extends Thread
{
	int id;
	int table_no;
	int chef_no;
	int order_no;
	int customer_id;
	Table ta;
	static Chef ch=new Chef(-1);
	
	Customer(int cc,Table t)
	{
		customer_id=cc;
		ta=t;
		start();
	}
	
	public void run()
	{
		ta.booktable(this);
		
		ch.bookchef(this);
		ch.cookfood(this);
		ch.leavechef(this);
		
		Timee.cust_start_eat(customer_id,System.currentTimeMillis());
		//wait for 30 minutes for customer to eat the food and leave the table. 
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
   
		ta.leavetable(this);
		Timee.cust_left_table(customer_id,System.currentTimeMillis());
	}
}

class Chef extends Thread
{
	int chef_id;
	int ready;//ready means free to serve anyone
	static int totalnumber;
	static Order[] o;
	static Chef[] ch;
	static MachineA ma = new MachineA();
	static MachineB mb = new MachineB();
	static MachineC mc = new MachineC();
	
	public Chef(int id){
		chef_id=id;
		ready=0;
		start();
	}
	
	static void copyorder(Order[] or)//makes the static copy of orders
	{
		o=or;
	}
	
	static void copychef(Chef[] chh)
	{
		ch=chh;
		totalnumber=chh.length;
		for(int i=0;i<totalnumber;i++)
			ch[i].ready=0;
	}
	
	boolean allBooked() // check if all the chefs are booked
	{
		int i;
		for(i=0;i<totalnumber;i++)
		{
			if(ch[i].ready==0)
				return false;
		}
		
		return true;
	}
	
	synchronized void bookchef(Customer c)
	{
		
		try{
			while(allBooked())
			{
				this.wait();
			}
			}catch(Exception e)
			{
				
			}

		
		int i;
		for(i=0;i<totalnumber;i++)
		{
			if(ch[i].ready==0)
			{
				c.chef_no=i;
				ch[i].ready=1;
				Timee.print_Cus_got_Chef(c.customer_id,i,System.currentTimeMillis());
			    break;
			}
		}
		
				
	}
	
	synchronized void leavechef(Customer c)
	{
		ch[c.chef_no].ready=0;
		this.notify();
		//should notify other customer
	}

	void cookfood(Customer c)
	{
		ma.getmachineA(c,o[c.customer_id].bur);
		ma.leavemachineA(c);
		
		mb.getmachineB(c,o[c.customer_id].fry);
		mb.leavemachineB(c);
		
		mc.getmachineC(c,o[c.customer_id].coke);
		mc.leavemachineC(c);
				
	}
}


public class Try extends Thread
{
public static void main(String args[]) throws FileNotFoundException
{
	int number_of_chefs;
	int number_of_customers;
	int number_of_tables;
	int wait_time,pre_wait_time;
	pre_wait_time = 0;
	
	
    Scanner scanner = new Scanner(System.in);
	//Scanner scanner = new Scanner(new File("data1.txt"));
    number_of_customers = scanner.nextInt();
    number_of_tables = scanner.nextInt();
    number_of_chefs = scanner.nextInt();
    
    Chef c[] = new Chef[number_of_chefs];
	Order o[] = new Order[number_of_customers];
	Customer cus[]=new Customer[number_of_customers];
	int arrival_time[] = new int[number_of_customers];
	
	//copy order array
	Table t = new Table(number_of_tables);
	
	for(int i=0;i<number_of_chefs;i++)
		c[i]=new Chef(i);
	
	for(int i=0;i<number_of_customers;i++)
	{
		arrival_time[i]=scanner.nextInt();
		o[i] = new Order(i,scanner.nextInt(),scanner.nextInt(),scanner.nextInt());
	}
	Chef.copychef(c);

	Chef.copyorder(o);
	
	Timee tt = new Timee(System.currentTimeMillis());
	
	for(int i=0;i<number_of_customers;i++)
	{
		wait_time = arrival_time[i];
		try {
			Thread.sleep((wait_time-pre_wait_time)*10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pre_wait_time = wait_time;
		Timee.print_Cust_Entered(i,System.currentTimeMillis());
		cus[i]=new Customer(i,t);
	}
		
}
}

