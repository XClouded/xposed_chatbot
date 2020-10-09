package com.uc.sandboxExport;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

@Api
/* compiled from: U4Source */
public interface IChildProcessSetup extends IInterface {
    IBinder preSetupConnection(Bundle bundle) throws RemoteException;

    @Api
    /* compiled from: U4Source */
    public static abstract class Stub extends Binder implements IChildProcessSetup {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.uc.sandboxExport.IChildProcessSetup");
        }

        public static IChildProcessSetup asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.uc.sandboxExport.IChildProcessSetup");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IChildProcessSetup)) {
                return new a(iBinder);
            }
            return (IChildProcessSetup) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.uc.sandboxExport.IChildProcessSetup");
                IBinder preSetupConnection = preSetupConnection(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                parcel2.writeStrongBinder(preSetupConnection);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString("com.uc.sandboxExport.IChildProcessSetup");
                return true;
            }
        }

        /* compiled from: U4Source */
        static class a implements IChildProcessSetup {
            private IBinder a;

            a(IBinder iBinder) {
                this.a = iBinder;
            }

            public final IBinder asBinder() {
                return this.a;
            }

            public final IBinder preSetupConnection(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.uc.sandboxExport.IChildProcessSetup");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readStrongBinder();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
