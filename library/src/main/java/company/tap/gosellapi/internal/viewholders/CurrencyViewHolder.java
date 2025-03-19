package company.tap.gosellapi.internal.viewholders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.api.models.AmountedCurrency;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.CurrencyViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.CurrencyViewModelData;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

/**
 * The type Currency view holder.
 */
public class CurrencyViewHolder extends PaymentOptionsBaseViewHolder<CurrencyViewModelData, CurrencyViewHolder, CurrencyViewModel> {

    private TextView currencyMainText;
    private TextView currencySecondaryText;
    private ImageView arrowIcon;

    /**
     * Instantiates a new Currency view holder.
     *
     * @param view the view
     */
    CurrencyViewHolder(View view) {

        super(view);

        currencyMainText = view.findViewById(R.id.currencyMainText);
        currencySecondaryText = view.findViewById(R.id.currencySecondaryText);
        arrowIcon = view.findViewById(R.id.arrowIcon);

        if (SDK_INT >= JELLY_BEAN_MR1) {
            if (view.getContext().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                arrowIcon.setBackgroundResource(R.drawable.ic_arrow_left_normal);
            }
        }
    }

    @Override
    public void bind(CurrencyViewModelData data) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(" currency model clicked...... " + viewModel.getData());
                viewModel.holderClicked();
            }
        });

        setTexts(data);
    }


    private void setTexts(CurrencyViewModelData data) {

        AmountedCurrency transactionCurrency    = data.getTransactionCurrency();
        AmountedCurrency selectedCurrency       = data.getSelectedCurrency();

//        System.out.println(" Currency View Holders : Utils.getFormattedCurrency : " +  selectedCurrency.getSymbol()+ " " + selectedCurrency.getAmount() );// Utils.getFormattedCurrency(selectedCurrency));
//        System.out.println(" Currency View Holders : CurrencyFormatter.format(selectedCurrency) : " + CurrencyFormatter.format(selectedCurrency));
        // replace CurrencyFormatter with Utils.getFormattedCurrency();
       if(selectedCurrency!=null && transactionCurrency!=null){
           String selectedCurrencyText = selectedCurrency.getSymbol()+ " " + selectedCurrency.getAmount();// Utils.getFormattedCurrency(selectedCurrency);

           if (transactionCurrency.getCurrency().equals(selectedCurrency.getCurrency())) {
               currencySecondaryText.setVisibility(View.GONE);
               currencySecondaryText.setText("");
           }
           else {

// Format currency text
               String transactionCurrencyText = transactionCurrency.getSymbol() + " " + transactionCurrency.getAmount();

// Check if the currency is SAR (Saudi Riyal)

                    transactionCurrencyText = transactionCurrency.getSymbol() + " " + transactionCurrency.getAmount();  //  Utils.getFormattedCurrency(transactionCurrency);

   currencySecondaryText.setVisibility(View.VISIBLE);
               currencySecondaryText.setText(transactionCurrencyText);
           }
           if (selectedCurrency.getSymbol().equalsIgnoreCase("SR") ||
                   selectedCurrency.getSymbol().equalsIgnoreCase("SAR") ||
                   selectedCurrency.getSymbol().equals("ر.س")) {
               Typeface customFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/sar-Regular.otf");

               // Replace currency symbol with "R"
               selectedCurrencyText = "R " + selectedCurrency.getAmount();

               // Apply custom font
               currencyMainText.setTypeface(customFont);
           }
           currencyMainText.setText(selectedCurrencyText);
       }




    }
}
