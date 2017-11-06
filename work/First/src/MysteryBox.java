import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.lang.instrument.Instrumentation;

//16 - object overhead
public class MysteryBox implements Serializable {
    private static volatile Instrumentation globalInstrumentation;
    
    private final double x0; //8
    private final int y0, y1; //8
    private final long z0; //8
    private final boolean[] a = new boolean[ 104 ]; //104 + 8 (reference to array) + 24 (array object overhead)
    //total = 128    
    
    public static void premain(final String agentArgs, final Instrumentation inst)
    {
       System.out.println("premain...");
       globalInstrumentation = inst;
    }
    public static void agentmain(String agentArgs, Instrumentation inst)
    {
       System.out.println("agentmain...");
       globalInstrumentation = inst;
    }
    
    public MysteryBox()
    {
        x0 = 12.2;
        y0 = 5; y1 = 7;
        z0 = 11199;
        a[1] = true;
        a[21] = true;
        a[12] = true;
        a[19] = true;
        a[55] = true;
        a[71] = true;
        a[103] = true;
    }
    
    public static void testSer() throws IOException
    {
        MysteryBox mb = new MysteryBox();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos); 
        out.writeObject(mb);
        byte[] res = bos.toByteArray();
        
        System.out.printf("%nObject size = %d bytes", res.length);
        //System.out.printf("%nInstron Object size = %d bytes", globalInstrumentation.getObjectSize(res));
    }
}
