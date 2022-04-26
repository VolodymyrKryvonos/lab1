package org.example.lab1p2.synchronyzed;

class Callme {
    synchronized void call(String msg) {
        System.out.print("[" + msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println("]");
    }
}

class Caller implements Runnable {
    String msg;
    final Callme target;
    Thread t;

    public Caller(Callme targ, String s, int priority) {
        target = targ;
        msg = s;
        t = new Thread(this);
        t.setPriority(priority);
        t.start();
    }

    @Override
    public void run() {
        target.call(msg);
    }
}

class Synch1 {
    public static void main(String args[]) {
        Callme target = new Callme();
        Caller ob1 = new Caller(target, "Kryvonos",10);
        Caller ob2 = new Caller(target, "Volodymyr",9);
        Caller ob3 = new Caller(target, "Victorovych",8);
        Caller ob4 = new Caller(target, "380632945567",7);
        Caller ob5 = new Caller(target, "kryvonosvolodymyr@gmail.com",6);

        try {
            ob1.t.join();
            ob2.t.join();
            ob3.t.join();
            ob4.t.join();
            ob5.t.join();
        }catch (Exception e){

        }
    }
}