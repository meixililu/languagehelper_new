/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.messi.languagehelper.aidl;
public interface IXBPlayer extends android.os.IInterface
{
  /** Default implementation for IXBPlayer. */
  public static class Default implements com.messi.languagehelper.aidl.IXBPlayer
  {
    @Override public void initAndPlay(java.lang.String data, boolean isPlayList, long currentPosition) throws android.os.RemoteException
    {
    }
    @Override public void initPlayList(java.lang.String lists, int position) throws android.os.RemoteException
    {
    }
    @Override public int getPlayStatus() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int getCurrentPosition() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int getDuration() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean MPlayerIsPlaying() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void MPlayerPause() throws android.os.RemoteException
    {
    }
    @Override public void MPlayerRestart() throws android.os.RemoteException
    {
    }
    @Override public void MPlayerSeekTo(int position) throws android.os.RemoteException
    {
    }
    @Override public boolean MPlayerIsSameMp3(java.lang.String oid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setLastPlayer(java.lang.String player) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getLastPlayer() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setAppExit(boolean isExit) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.messi.languagehelper.aidl.IXBPlayer
  {
    private static final java.lang.String DESCRIPTOR = "com.messi.languagehelper.aidl.IXBPlayer";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.messi.languagehelper.aidl.IXBPlayer interface,
     * generating a proxy if needed.
     */
    public static com.messi.languagehelper.aidl.IXBPlayer asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.messi.languagehelper.aidl.IXBPlayer))) {
        return ((com.messi.languagehelper.aidl.IXBPlayer)iin);
      }
      return new com.messi.languagehelper.aidl.IXBPlayer.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_initAndPlay:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          long _arg2;
          _arg2 = data.readLong();
          this.initAndPlay(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_initPlayList:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.initPlayList(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getPlayStatus:
        {
          data.enforceInterface(descriptor);
          int _result = this.getPlayStatus();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getCurrentPosition:
        {
          data.enforceInterface(descriptor);
          int _result = this.getCurrentPosition();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getDuration:
        {
          data.enforceInterface(descriptor);
          int _result = this.getDuration();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_MPlayerIsPlaying:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.MPlayerIsPlaying();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_MPlayerPause:
        {
          data.enforceInterface(descriptor);
          this.MPlayerPause();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_MPlayerRestart:
        {
          data.enforceInterface(descriptor);
          this.MPlayerRestart();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_MPlayerSeekTo:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.MPlayerSeekTo(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_MPlayerIsSameMp3:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.MPlayerIsSameMp3(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setLastPlayer:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.setLastPlayer(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getLastPlayer:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getLastPlayer();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_setAppExit:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setAppExit(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.messi.languagehelper.aidl.IXBPlayer
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void initAndPlay(java.lang.String data, boolean isPlayList, long currentPosition) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(data);
          _data.writeInt(((isPlayList)?(1):(0)));
          _data.writeLong(currentPosition);
          boolean _status = mRemote.transact(Stub.TRANSACTION_initAndPlay, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().initAndPlay(data, isPlayList, currentPosition);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void initPlayList(java.lang.String lists, int position) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(lists);
          _data.writeInt(position);
          boolean _status = mRemote.transact(Stub.TRANSACTION_initPlayList, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().initPlayList(lists, position);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getPlayStatus() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPlayStatus, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPlayStatus();
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getCurrentPosition() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCurrentPosition, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCurrentPosition();
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getDuration() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getDuration, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getDuration();
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean MPlayerIsPlaying() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_MPlayerIsPlaying, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().MPlayerIsPlaying();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void MPlayerPause() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_MPlayerPause, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().MPlayerPause();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void MPlayerRestart() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_MPlayerRestart, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().MPlayerRestart();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void MPlayerSeekTo(int position) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(position);
          boolean _status = mRemote.transact(Stub.TRANSACTION_MPlayerSeekTo, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().MPlayerSeekTo(position);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean MPlayerIsSameMp3(java.lang.String oid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(oid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_MPlayerIsSameMp3, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().MPlayerIsSameMp3(oid);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setLastPlayer(java.lang.String player) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(player);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLastPlayer, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setLastPlayer(player);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getLastPlayer() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLastPlayer, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getLastPlayer();
          }
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setAppExit(boolean isExit) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((isExit)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAppExit, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setAppExit(isExit);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.messi.languagehelper.aidl.IXBPlayer sDefaultImpl;
    }
    static final int TRANSACTION_initAndPlay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_initPlayList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getPlayStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getCurrentPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getDuration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_MPlayerIsPlaying = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_MPlayerPause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_MPlayerRestart = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_MPlayerSeekTo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_MPlayerIsSameMp3 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_setLastPlayer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getLastPlayer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_setAppExit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    public static boolean setDefaultImpl(com.messi.languagehelper.aidl.IXBPlayer impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.messi.languagehelper.aidl.IXBPlayer getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void initAndPlay(java.lang.String data, boolean isPlayList, long currentPosition) throws android.os.RemoteException;
  public void initPlayList(java.lang.String lists, int position) throws android.os.RemoteException;
  public int getPlayStatus() throws android.os.RemoteException;
  public int getCurrentPosition() throws android.os.RemoteException;
  public int getDuration() throws android.os.RemoteException;
  public boolean MPlayerIsPlaying() throws android.os.RemoteException;
  public void MPlayerPause() throws android.os.RemoteException;
  public void MPlayerRestart() throws android.os.RemoteException;
  public void MPlayerSeekTo(int position) throws android.os.RemoteException;
  public boolean MPlayerIsSameMp3(java.lang.String oid) throws android.os.RemoteException;
  public void setLastPlayer(java.lang.String player) throws android.os.RemoteException;
  public java.lang.String getLastPlayer() throws android.os.RemoteException;
  public void setAppExit(boolean isExit) throws android.os.RemoteException;
}
