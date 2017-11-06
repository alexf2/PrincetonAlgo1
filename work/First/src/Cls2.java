
public class Cls2 {
    String A;
    final int bB;
	
	public Cls2()
	{	
		bB = 17;
	}
	
	int getA()
	{
		return  Integer.parseInt(A);
	}
	
	public final int getB()
	{
		return bB;
	}
	
	class Cls3 extends Cls2
	{
	    @Override
	    int getA()
	    {
	        return super.getA() * 2;
	    }   
	}
}



