package com.abel.app.b2b.model.response;

import java.io.Serializable;

public class RIequipment implements Serializable {
   public int TypeOf,EquipInventId,Price;

   public String EquipInventSerialNo;
   public int Quantity,Amount;

   public RIequipment() {
   }
   public RIequipment(int typeOf, int equipInventId, int price) {
      TypeOf = typeOf;
      EquipInventId = equipInventId;
      Price = price;
   }

   public RIequipment(int typeOf,String equipInventSerialNo, int equipInventId, int quantity, int price,  int amount) {
      TypeOf = typeOf;
      EquipInventId = equipInventId;
      Price = price;
      EquipInventSerialNo = equipInventSerialNo;
      Quantity = quantity;
      Amount = amount;
   }
}
