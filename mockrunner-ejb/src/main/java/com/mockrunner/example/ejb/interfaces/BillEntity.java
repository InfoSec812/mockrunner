/*
 * Generated by XDoclet - Do not edit!
 */
package com.mockrunner.example.ejb.interfaces;

/**
 * Remote interface for BillEntity.
 */
public interface BillEntity
   extends javax.ejb.EJBObject
{

   java.lang.Integer getId()
      throws java.rmi.RemoteException;

   java.util.Date getDate()
      throws java.rmi.RemoteException;

   void setDate(java.util.Date date)
      throws java.rmi.RemoteException;

   boolean getPaid()
      throws java.rmi.RemoteException;

   void setPaid(boolean isPaid)
      throws java.rmi.RemoteException;

}
