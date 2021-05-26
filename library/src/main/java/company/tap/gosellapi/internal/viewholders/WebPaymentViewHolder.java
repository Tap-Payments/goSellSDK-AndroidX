package company.tap.gosellapi.internal.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.activities.AsynchronousPaymentActivity;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.WebPaymentViewModel;
import mobi.foo.benefitinapp.data.Transaction;
import mobi.foo.benefitinapp.listener.BenefitInAppButtonListener;
import mobi.foo.benefitinapp.listener.CheckoutListener;
import mobi.foo.benefitinapp.utils.BenefitInAppButton;
import mobi.foo.benefitinapp.utils.BenefitInAppCheckout;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

/**
 * The type Web payment view holder.
 */
public class WebPaymentViewHolder
        extends PaymentOptionsBaseViewHolder<PaymentOption, WebPaymentViewHolder, WebPaymentViewModel> implements CheckoutListener {
    private ImageView paymentSystemIcon;
    private TextView paymentSystemName;
    private ImageView arrowIcon;
    private BenefitInAppButton benefitInAppButton;
    String appId="4530082749";
    String merchantId="00000101";
    String seceret="3l5e0cstdim11skgwoha8x9vx9zo0kxxi4droryjp4eqd";
    String countrycode="1001";
    String mcc="4816";
    private Context context;
    private Activity activity;

    /**
     * Instantiates a new Web payment view holder.
     *
     * @param itemView the item view
     */
    WebPaymentViewHolder(final View itemView) {
        super(itemView);
        paymentSystemIcon = itemView.findViewById(R.id.paymentSystemIcon);
        paymentSystemName = itemView.findViewById(R.id.paymentSystemName);
        arrowIcon = itemView.findViewById(R.id.arrowIcon);
        this.benefitInAppButton = itemView.findViewById(R.id.benefit_pay_btn);
        if (SDK_INT >= JELLY_BEAN_MR1) {
            if (itemView.getContext().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                arrowIcon.setBackgroundResource(R.drawable.ic_arrow_left_normal);
            }
        }
        this.activity = (Activity) itemView.getContext();
        this.benefitInAppButton.setListener(new BenefitInAppButtonListener() {
            @Override
            public void onButtonClicked() {

                BenefitInAppCheckout.newInstance(activity,
                        appId,
                        "343432",
                        merchantId,
                        seceret,
                        "10",
                        "BH",
                        "048",
                        mcc,
                        "Testing Tap",
                        "Sadad",
                        new CheckoutListener() {
                            @Override
                            public void onTransactionSuccess(Transaction transaction) {
                                System.out.println("onTransactionSuccess"+transaction);
                            }

                            @Override
                            public void onTransactionFail(Transaction transaction) {
                                System.out.println("onTransactionFail"+transaction);

                            }
                        });

            }

            @Override
            public void onFail(int i) {
                System.out.println("onFail benefit:"+i);
            }
        });

    }

    @Override
    public void bind(PaymentOption data) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.itemClicked();
            }
        });

        paymentSystemName.setText(data.getName());
        Glide.with(itemView.getContext()).load(data.getImage()).into(paymentSystemIcon);
        if(viewModel!=null)
            viewModel.setWebViewHolder(this);


    }

    public void disableWebClick(){
        System.out.println("WebviewHolder disabled");
        itemView.setEnabled(false);
        itemView.setFocusableInTouchMode(false);
        itemView.setClickable(false);


    }
    public void enableWebClick(){
        itemView.setEnabled(true);
        itemView.setClickable(true);
        itemView.setFocusableInTouchMode(true);
    }


    @Override
    public void onTransactionSuccess(Transaction transaction) {
        System.out.println("onTransactionSuccess"+transaction);

    }

    @Override
    public void onTransactionFail(Transaction transaction) {
        System.out.println("onTransactionFail"+transaction);

    }
}
