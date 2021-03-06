/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kodemat.visu.unity.network;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuMarking;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.VisuComponent;
import org.openide.util.Exceptions;

/**
 *
 * @author messe
 */
public class KodematSocketServer extends AbstractServer {

    VisuHelper helper;
    HazelcastInstance hz;
    Map<SelectionKey, UnityScenegraphSynchronizer> synchronizers = new HashMap<>();

    public KodematSocketServer() {
        super(6104, new ByteMessageLength(), 512);
    }

    @Override
    protected void messageReceived(ByteBuffer message, SelectionKey key) {
        System.out.print("Message received:");
        String msg;
        try {
            msg = new String(message.array(), "utf8");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
            msg = "Exception: " + ex.getMessage();
        }
        System.out.println(msg);
        msg = msg.trim();

        try {
            if (msg.startsWith("ID")) {
                String[] split = msg.split(" ");
                long id = Long.parseLong(split[1]);
                VisuComponent comp = helper.getComponent(id);

                if (comp != null) {

                    if ("DELETE".equals(split[2])) {
                        helper.deleteComponent(comp);
                    }
                    
                    if ("TYPE".equals(split[2])) {
                        String type = split[4];
                        String path = split[6];
                        comp.setType(new VisuType(type, path));
                    }                    
                    
                    if ("PARENT".equals(split[2])) {
                        Long pid = Long.parseLong(split[4]);
                        comp.setParent(pid);
                    }


                    if ("TRANSLATION".equals(split[2])) {

                        float x = Float.parseFloat(split[4]);
                        float y = Float.parseFloat(split[6]);
                        float z = Float.parseFloat(split[8]);

                        comp.setTranslation(new VisuVector3f(x, y, z));

                    }
                    
                    if ("ROTATION".equals(split[2])) {

                        float x = Float.parseFloat(split[4]);
                        float y = Float.parseFloat(split[6]);
                        float z = Float.parseFloat(split[8]);

                        comp.setRotation(new VisuRotation(x, y, z));

                    }
                    
                    if ("SCALE".equals(split[2])) {

                        float x = Float.parseFloat(split[4]);
                        float y = Float.parseFloat(split[6]);
                        float z = Float.parseFloat(split[8]);

                        comp.setScale(new VisuVector3f(x, y, z));

                    }
                    
                    if ("MARKING".equals(split[2])) {

                        int boundingBox = Integer.parseInt(split[3]);
                                                                       
                        String label = "";
                        
                        for(int i = 5; i<split.length; i++){
                            label += " " + split[i];
                        }
                        
                        label = label.trim();

                        if("EMPTY".equals(label)){
                            label = "";                            
                        }
                        
                        VisuMarking vm = new VisuMarking(label, boundingBox);
                        comp.setMarking(vm);
                        comp.setLabel(label);

                    }

                } else {
                    if ("CREATE".equals(split[2]) && "ID".equals(split[3])) {
                        helper.createComponent(id, split[4]);
                    } else {
                        System.out.println("Component " + id + " does not exist");
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("Exception while parsing message: " + ex.getMessage());
            ex.printStackTrace();
        }



    }

    @Override
    protected void connection(SelectionKey key) {
        try {
            System.out.println("Connection opened to " + ((SocketChannel) key.channel()).getRemoteAddress().toString());

            synchronizers.put(key, new UnityScenegraphSynchronizer(helper, key, this));

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            System.out.println("Connection open exception: " + ex.getMessage());
        }
    }

    @Override
    protected void disconnected(SelectionKey key) {
        try {
            System.out.println("Connection closed to " + ((SocketChannel) key.channel()).getRemoteAddress().toString());
            UnityScenegraphSynchronizer syn = synchronizers.remove(key);
            if (syn != null) {
                syn.dispose();
            }

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            System.out.println("Connection closed exception: " + ex.getMessage());
        }
    }

    @Override
    protected void started(boolean alreadyStarted) {
        System.out.println("Server started");
    }

    @Override
    protected void stopped() {
        System.out.println("Server stopped");
    }

    public Config xmlFileBuild(String FileName) {

        Config cfg_test = new Config();
        try {
            cfg_test = new XmlConfigBuilder(FileName).build();
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());

        }
        return cfg_test;
    }

    public void server_Start() {
        Config cfg = xmlFileBuild("./hazelcastServer.xml");
        hz = Hazelcast.newHazelcastInstance(cfg);
        helper = new VisuHelper(hz, true, false);



        run();

    }

    public static void main(String[] args) {

        KodematSocketServer server = new KodematSocketServer();
        server.server_Start();
    }
}
