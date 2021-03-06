package org.slave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.hyperic.sigar.*;
import com.alibaba.fastjson.*;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;

import static com.alibaba.fastjson.JSON.toJSONString;

public class Report {
    public static String reportCPU() {
        class CPU{
            Integer core;

            public Integer getCore() {
                return core;
            }

            public void setCore(Integer core) {
                this.core = core;
            }

            public Integer getFrequency() {
                return frequency;
            }

            public void setFrequency(Integer frequency) {
                this.frequency = frequency;
            }

            Integer frequency;
        }
        try {
            CPU cpu=new CPU();
            String shpath = "/home/legendhu/IdeaProjects/Serverless/testCPU.sh";
            Process ps = Runtime.getRuntime().exec(shpath);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String result = sb.toString();
            System.out.println(result);
            cpu.core=Integer.parseInt(result.substring(6,7));
            String tmp=result.substring(result.length()-7,result.length()-3);
            Float tmp2=Float.parseFloat(tmp)*1000;
            cpu.frequency=(int)tmp2.floatValue();
            result=toJSONString(cpu);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String reportRAM() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        System.out.println(mem.getTotal()/ 1024L/ 1024L);
        System.out.println(mem.getFree()/ 1024L/ 1024L);
        System.out.println(mem.getRam());
        return null;
    }

//    public static String reportRAM(){
//
//    }

    public static void main(String[] args) throws SigarException, IOException {
        reportCPU();
    }
}