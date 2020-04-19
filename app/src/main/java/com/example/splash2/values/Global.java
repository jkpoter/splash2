package com.example.splash2.values;

public class Global {
    private Global global = null;
    public String server="http://26.243.14.255/";
public Global getglobal(){
    if(global==null){
      global = new Global();
    }
        return global;

}
}
