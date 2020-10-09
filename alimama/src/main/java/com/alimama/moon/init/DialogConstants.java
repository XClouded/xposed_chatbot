package com.alimama.moon.init;

public class DialogConstants {

    public enum TYPE {
        TAOPASSWORD(20),
        MARKET(50),
        FORCEUPADATE(10),
        UPDATE(41),
        NOTIFACATION(40);
        
        private int priority;

        private TYPE(int i) {
            this.priority = i;
        }

        public int getPriority() {
            return this.priority;
        }

        public void setPriority(int i) {
            this.priority = i;
        }
    }
}
