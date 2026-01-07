package company.tap.gosellapi.internal.data_managers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.Iterator;

import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.SaveCard;
import company.tap.gosellapi.internal.api.models.Token;
import company.tap.gosellapi.internal.interfaces.IPaymentProcessListener;
@RestrictTo(RestrictTo.Scope.LIBRARY)
 class PaymentProcessListener implements IPaymentProcessListener {

    @Override
    public void didReceiveCharge( @NonNull Charge charge) {
        Log.d("PaymentDataManager","didReceiveCharge started................");
        if(getListeners()!=null)
            Log.d("PaymentDataManager","come to didReceiveCharge................"+getListeners().size());

        for (Iterator i = getListeners().iterator(); i.hasNext();) {
            try{
                Log.d("PaymentdataManager","try to cast listener......  ");
                IPaymentProcessListener listener = (IPaymentProcessListener) i.next();
                Log.d("PaymentdataManager","listener : ["+listener+"]");
                listener.didReceiveCharge(charge);

            }catch (Exception e){
                Log.d("PaymentDataManager","didReceiveCharge exception : ["+e.getLocalizedMessage()+"]");
                Log.d("PaymentDataManager","didReceiveCharge exception : ["+e.getMessage()+"]");
                Log.d("PaymentDataManager","didReceiveCharge exception : ["+e.getCause()+"]");
                Log.d("PaymentDataManager","didReceiveCharge :: exception while looping over listeners");
                continue;
            }
        }
    }

    @Override
    public void didReceiveAuthorize( @NonNull Authorize authorize) {
        //  Log.d("PaymentDataManager","didReceiveAuthorize started................");
        if(getListeners()!=null)
            //  Log.d("PaymentDataManager","come to didReceiveAuthorize................"+getListeners().size());
            for (Iterator i = getListeners().iterator(); i.hasNext();) {
                try{
                    //  Log.d("PaymentDataManager","try to cast listener......  ");
                    IPaymentProcessListener listener = (IPaymentProcessListener) i.next();
                    //  Log.d("PaymentdataManager","listener : ["+listener+"]");
                    listener.didReceiveAuthorize(authorize);
                }catch (Exception e){
                    Log.d("PaymentDataManager","didReceiveAuthorize exception : ["+e.getLocalizedMessage()+"]");
                    Log.d("PaymentDataManager","didReceiveAuthorize exception : ["+e.getMessage()+"]");
                    Log.d("PaymentDataManager","didReceiveAuthorize exception : ["+e.getCause()+"]");
                    Log.d("PaymentDataManager","didReceiveAuthorize :: exception while looping over listeners");
                    continue;
                }
            }
    }

    @Override
    public void didReceiveSaveCard( @NonNull SaveCard saveCard) {
        //  Log.d("PaymentDataManager","didReceiveSaveCard started................");
        //

        if(getListeners()!=null) //Log.d("PaymentDataManager","come to didReceiveSaveCard................"+getListeners().size());
            for (Iterator i = getListeners().iterator(); i.hasNext();) {
                try{
                    //  Log.d("PaymentDataManager","try to cast listener......  ");
                    IPaymentProcessListener listener = (IPaymentProcessListener) i.next();
                    //   Log.d("PaymentdataManager","listener : ["+listener+"]");
                    listener.didReceiveSaveCard(saveCard);
                }catch (Exception e){
                    Log.d("PaymentDataManager","didReceiveSaveCard exception : ["+e.getLocalizedMessage()+"]");
                    Log.d("PaymentDataManager","didReceiveSaveCard exception : ["+e.getMessage()+"]");
                    Log.d("PaymentDataManager","didReceiveSaveCard exception : ["+e.getCause()+"]");
                    Log.d("PaymentDataManager","didReceiveSaveCard :: exception while looping over listeners");
                    continue;
                }
            }

    }

    @Override
    public void didReceiveError(@NonNull GoSellError error) {
        Log.d("PaymentDataManager","didReceiveError started................");
        if(getListeners()!=null)
            Log.d("PaymentDataManager","come to didReceiveSaveCard................"+getListeners().size());
        for (Iterator i = getListeners().iterator(); i.hasNext();) {
            try{
                Log.d("PaymentDataManager","try to cast listener......  ");
                IPaymentProcessListener listener = (IPaymentProcessListener) i.next();
                Log.d("PaymentdataManager","listener : ["+listener+"]");
                listener.didReceiveError(error);
            }catch (Exception e){
                Log.d("PaymentDataManager","didReceiveError exception : ["+e.getLocalizedMessage()+"]");
                Log.d("PaymentDataManager","didReceiveError exception : ["+e.getMessage()+"]");
                Log.d("PaymentDataManager","didReceiveError exception : ["+e.getCause()+"]");
                Log.d("PaymentDataManager","didReceiveError :: exception while looping over listeners");
                continue;
            }
        }
    }


    @Override
    public void didCardSavedBefore() {
        Log.d("PaymentDataManager","didCardSavedBefore started................");
        if(getListeners()!=null)
            Log.d("PaymentDataManager","come to didReceiveSaveCard................"+getListeners().size());
        for (Iterator i = getListeners().iterator(); i.hasNext();) {
            try{
                Log.d("PaymentDataManager","try to cast listener......  ");
                IPaymentProcessListener listener = (IPaymentProcessListener) i.next();
                Log.d("PaymentdataManager","listener : ["+listener+"]");
                listener.didCardSavedBefore();
            }catch (Exception e){
                Log.d("PaymentDataManager","didCardSavedBefore exception : ["+e.getLocalizedMessage()+"]");
                Log.d("PaymentDataManager","didCardSavedBefore exception : ["+e.getMessage()+"]");
                Log.d("PaymentDataManager","didCardSavedBefore exception : ["+e.getCause()+"]");
                Log.d("PaymentDataManager","didCardSavedBefore :: exception while looping over listeners");
                continue;
            }
        }

    }

    @Override
    public void fireCardTokenizationProcessCompleted(@NonNull Token token) {
        Log.d("PaymentDataManager","fireCardTokenizationProcessCompleted started................");
        if(getListeners()!=null)
            Log.d("PaymentDataManager","come to fireCardTokenizationProcessCompleted................"+getListeners().size());
        for (Iterator i = getListeners().iterator(); i.hasNext();) {
            try{
                Log.d("PaymentDataManager","try to cast listener......  ");
                IPaymentProcessListener listener = (IPaymentProcessListener) i.next();
                Log.d("PaymentDataManager","listener : ["+listener+"]");
                listener.fireCardTokenizationProcessCompleted(token);
            }catch (Exception e){
                Log.d("PaymentDataManager","fireCardTokenizationProcessCompleted exception : ["+e.getLocalizedMessage()+"]");
                Log.d("PaymentDataManager","fireCardTokenizationProcessCompleted exception : ["+e.getMessage()+"]");
                Log.d("PaymentDataManager","fireCardTokenizationProcessCompleted exception : ["+e.getCause()+"]");
                Log.d("PaymentDataManager","fireCardTokenizationProcessCompleted:: exception while looping over listeners");
                continue;
            }
        }
    }


    protected void addListener(IPaymentProcessListener listener) {

        getListeners().add(listener);
    }

    private void removeListener(IPaymentProcessListener listener) {

        getListeners().remove(listener);
    }

    @NonNull
    private ArrayList<IPaymentProcessListener> listeners;

    public PaymentProcessListener() {

        this.listeners = new ArrayList<>();
    }

    public ArrayList<IPaymentProcessListener> getListeners() {

        return listeners;
    }
}
