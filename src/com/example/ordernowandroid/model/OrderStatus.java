package com.example.ordernowandroid.model;

public enum OrderStatus {
    ModifiedOrder {
        @Override
        public String getSymbol() {
            return "U";
        }
    },
    Recieved {
        @Override
        public String getSymbol() {
            return "R";
        }
    },
    Accepted {
        @Override
        public String getSymbol() {
            return "A";
        }
    },
    Sent {
        @Override
        public String getSymbol() {
            return "S";
        }
    }, Complete {
        @Override
        public String getSymbol() {
            return "F";
        }
    };
    ;
    public abstract String getSymbol();
}
