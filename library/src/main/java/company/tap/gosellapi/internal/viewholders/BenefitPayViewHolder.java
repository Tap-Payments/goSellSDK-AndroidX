package company.tap.gosellapi.internal.viewholders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.adapters.RecentPaymentsRecyclerViewAdapter;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.GroupViewModel;
import mobi.foo.benefitinapp.data.Transaction;
import mobi.foo.benefitinapp.listener.BenefitInAppButtonListener;
import mobi.foo.benefitinapp.listener.CheckoutListener;
import mobi.foo.benefitinapp.utils.BenefitInAppButton;
import mobi.foo.benefitinapp.utils.BenefitInAppCheckout;

/**
 * Created by AhlaamK on 5/25/21.
 * <p>
 * Copyright (c) 2021    Tap Payments.
 * All rights reserved.
 **/
class BenefitPayViewHolder extends PaymentOptionsBaseViewHolder<String, GroupViewHolder, GroupViewModel> implements CheckoutListener {

    private BenefitInAppButton benefitInAppButton;
    String appId="4530082749";
    String merchantId="00000101";
    String seceret="3l5e0cstdim11skgwoha8x9vx9zo0kxxi4droryjp4eqd";
    String countrycode="1001";
    String mcc="4816";
    private Context context;
    private Activity activity;

    /**
     * Instantiates a new Group view holder.
     *
     * @param view the view
     */
    public BenefitPayViewHolder(View view) {

        super(view);
        this.benefitInAppButton = view.findViewById(R.id.benefit_pay_btn);
        this.context = view.getContext();
        this.activity = (Activity) view.getContext();

       /* this.benefitInAppButton.setOnClickListener(v -> {


        });*/

        this.benefitInAppButton.setListener(new BenefitInAppButtonListener() {
            @Override
            public void onButtonClicked() {
                BenefitInAppCheckout.newInstance(
                        activity,
                         appId,
                        "445544",
                        merchantId,
                        seceret,
                        "10.000",
                        "BH",
                        "048",
                        mcc,
                        "Testing Tap",
                        "Sadad",
                        new CheckoutListener() {
                            @Override
                            public void onTransactionSuccess(Transaction transaction) {
                                System.out.println("transaction success value are"+transaction);

                            }

                            @Override
                            public void onTransactionFail(Transaction transaction) {
                                System.out.println("transaction failure value are"+transaction);

                            }
                        });

            }

            @Override
            public void onFail(int i) {

            }
        });


    }

    @Override
    public void bind(String data) {

     //   if(viewModel!=null)
          //  viewModel.setGroupViewHolder(this);
    }

    @Override
    public void onTransactionSuccess(Transaction transaction) {

    }

    @Override
    public void onTransactionFail(Transaction transaction) {

    }




  /*  @Override
    public void changeGroupActionTitle() {
        this.cancelTextView.setVisibility(View.INVISIBLE);
        this.editTextView.setVisibility(View.VISIBLE);
    }*/

}