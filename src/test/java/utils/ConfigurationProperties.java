package utils;

import java.io.InputStream;
import java.util.Properties;
//class sozdaet ob`ekt properties schitivaia fail default.properties,
//but we have opportunity to override it with pom.xml and pom has privilege to compare with .properties file.


public class ConfigurationProperties {

    private static final String DEAFAULT_PROPERTIES="config/default.properties";
    private static Properties properties;

    public static void initializeProperties(){

        //load default properties from default.properties
        properties=loadProperties();
        //check for any override
        for(String key : properties.stringPropertyNames()){
            //if(System.getProperties().contains(key)){//proverka na nalichie 'key' v cmd, esli on est znachit ego override s pomosch`y comand line
            if(System.getProperty(key)!=null){
                properties.setProperty(key,System.getProperty(key));
            }
        }
        System.out.println("**************** PROPERTIES THAT HAS BEEN USED ***************************");
        for(String key : properties.stringPropertyNames()){
            System.out.println(key+" - "+properties.getProperty(key));
        }
        System.out.println("************************************************************************");
    }

    public static String get(String key){
        return properties.getProperty(key);
    }

    private static Properties loadProperties(){
        Properties properties=new Properties();
        try(InputStream stream = ResourceLoader.getResources(DEAFAULT_PROPERTIES)){
            properties.load(stream);
        }catch (Exception e){
            System.out.println("not able to read properties file from default.properties package");
        }
        return properties;
    }

}
