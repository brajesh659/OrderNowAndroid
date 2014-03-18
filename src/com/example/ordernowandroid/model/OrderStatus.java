package com.example.ordernowandroid.model;


public enum OrderStatus {
    ModifiedOrder {
        @Override
        public String getSymbol() {
            return "U";
        }
        
        @Override
        public OrderStatus newStatus(OrderStatus itemNewStatus) {
            if(itemNewStatus==OrderStatus.Complete)
                return ModifiedOrder;
            return itemNewStatus;
        }
    },
    Recieved {
        @Override
        public String getSymbol() {
            return "R";
        }
        
        @Override
        public OrderStatus newStatus(OrderStatus itemNewStatus) {
            return itemNewStatus;
        }
    },
    Accepted {
        @Override
        public String getSymbol() {
            return "A";
        }
        
        @Override
        public OrderStatus newStatus(OrderStatus itemNewStatus) {
            return itemNewStatus;
        }
    },
    Sent {
        @Override
        public String getSymbol() {
            return "S";
        }

        @Override
        public OrderStatus newStatus(OrderStatus itemNewStatus) {
            return itemNewStatus;
        }
    }, Complete {
        @Override
        public String getSymbol() {
            return "F";
        }

        @Override
        public OrderStatus newStatus(OrderStatus itemNewStatus) {
            return OrderStatus.Complete;
        }
    }, NULL {
        @Override
        public String getSymbol() {
            return "";
        }

        @Override
        public OrderStatus newStatus(OrderStatus itemCurrentStatus) {
            return itemCurrentStatus;
        }
    };
    ;
    public abstract String getSymbol();
    public abstract OrderStatus newStatus(OrderStatus itemNewStatus);

}
