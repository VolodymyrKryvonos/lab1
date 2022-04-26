package org.example.lab1p2.not_synch;

class Callme {
    void call(String msg) {
        System.out.print("[" + msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException е) {
            System.out.println("Перервано");
        }
        System.out.println("]");
    }
}

class Caller implements Runnable {
    String msg;
    Callme target;
    Thread t;

    public Caller(Callme targ, String s) {
        target = targ;
        msg = s;
        t = new Thread(this);
        t.start();
    }

    public void run() {
        target.call(msg);
    }
}

class Synch {
    public static void main(String args[]) {
        Callme target = new Callme();
        Caller ob1 = new Caller(target, "Kryvonos");
        Caller ob2 = new Caller(target, "Volodymyr");
        Caller ob3 = new Caller(target, "Victorovych");
        Caller ob4 = new Caller(target, "380632945567");
        Caller ob5 = new Caller(target, "kryvonosvolodymyr@gmail.com");
// wait for threads to end
        try {
            ob1.t.join();
            ob2.t.join();
            ob3.t.join();
            ob4.t.join();
            ob5.t.join();
        } catch (InterruptedException е) {
            System.out.println("Перервано");
        }
    }
}
