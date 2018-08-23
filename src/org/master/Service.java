package org.master;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import org.pojo.Node;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.connection.DBConn.getconn;
import static org.master.Algorithm.getUUID;
import static org.master.Algorithm.selectServiceMachine;
import static org.master.Utils_joinCluster.execSQL;
import static org.master.Utils_joinCluster.parseJson;

public class Service {

    private static final String SERVER_ADDRESS = "127.0.0.1";    //中心服务器的ip
    private static final Integer SERVER_PORT = 6379;         //端口号
    private String driver;
    public String createServerService(String serviceName) {
        String serviceId = "error";
        if (serviceName != null) {
            Jedis jedis = new Jedis(SERVER_ADDRESS, SERVER_PORT);
            if(jedis.exists("key1")){
                return serviceId;
            }
            serviceId= getUUID();
            Utils_createServerservice.mkdir(serviceId);
            jedis.set(serviceName, serviceId);
        }
        return serviceId;
    }

    public String findService(String serviceId) throws SQLException {
        Connection conn=getconn();
        String sql="select * FROM relationship WHERE serviceId=(?)";
        PreparedStatement ps;
        ResultSet rs=null;
        ps=(PreparedStatement) conn.prepareStatement(sql);
        ps.setString(1,serviceId);
        rs=ps.executeQuery();
        List<String> ipList=new ArrayList<>();
        while (rs.next()){
            ipList.add(rs.getString("nodeIP"));
        }
        if(ipList.isEmpty()){
            String appropriateIP=hatchService(serviceId);
            return appropriateIP;
        }
        String appropriateIP=selectServiceMachine(ipList);
        return appropriateIP;
    }

    //
    public String hatchService(String serviceId) throws SQLException {
        String newNodeIP=Algorithm.selectLoadMachine(serviceId);

        return newNodeIP;
    }

    public String joinCluster(String machineData) throws SQLException {
        Node node = parseJson(machineData);
        execSQL(node);
        return "aaa";
    }

    public static void main(String[] args) throws Exception {
        //-----接收郑杰他们传过来的serviceName

        Service s = new Service();
        while (args != null) {
            String serviceId = getUUID();
//            org.master.Service.createServerService();
        }
    }
}