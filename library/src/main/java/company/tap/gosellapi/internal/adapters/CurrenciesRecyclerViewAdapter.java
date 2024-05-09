package company.tap.gosellapi.internal.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.regex.Pattern;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.api.models.AmountedCurrency;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.CurrencyViewModelData;
import company.tap.gosellapi.internal.interfaces.CurrenciesAdapterCallback;
import company.tap.gosellapi.internal.utils.LocalizedCurrency;
import company.tap.gosellapi.internal.utils.Utils;
import company.tap.gosellapi.internal.viewholders.CurrencyViewHolder;
import company.tap.gosellapi.internal.viewholders.PaymentOptionsBaseViewHolder;

/**
 * The type Currencies recycler view adapter.
 */
public class CurrenciesRecyclerViewAdapter extends RecyclerView.Adapter<CurrenciesRecyclerViewAdapter.CurrencyCellViewHolder> {

    private CurrenciesAdapterCallback callback;
    private final static int NO_SELECTION = -1;
    private ArrayList<LocalizedCurrency> allCurrencies;
    private ArrayList<LocalizedCurrency> filteredCurrencies;
    private LocalizedCurrency selectedCurrency;
    private String searchQuery = null;
    private ArrayList<LocalizedCurrency> getAllCurrencies() { return allCurrencies; }
    private ArrayList<LocalizedCurrency> getFilteredCurrencies() { return filteredCurrencies; }
    private LocalizedCurrency getSelectedCurrency() { return selectedCurrency; }
    private String getSearchQuery() { return searchQuery; }
    private TextView tvAmountCurrencyName;
    private int selectedPosition;

    /**
     * Instantiates a new Currencies recycler view adapter.
     *
     * @param allCurrencies    the all currencies
     * @param selectedCurrency the selected currency
     * @param callback         the callback
     */
    public CurrenciesRecyclerViewAdapter(ArrayList<AmountedCurrency> allCurrencies, AmountedCurrency selectedCurrency, CurrenciesAdapterCallback callback,int selectedPosition) {

        this.allCurrencies = new ArrayList<>();

        LocalizedCurrency selected = null;

        for ( AmountedCurrency currency : allCurrencies) {

            LocalizedCurrency localizedCurrency = new LocalizedCurrency(currency);
            this.allCurrencies.add(localizedCurrency);

            if ( currency.equals(selectedCurrency) ) {

                selected = localizedCurrency;
            }
        }

        if ( selected == null ) {

            for (int i = 0; i < allCurrencies.size() ; i++) {
                if(selectedCurrency.getCurrency().equalsIgnoreCase(allCurrencies.get(i).getCurrency())){
                    selected = this.allCurrencies.get(i);
                }

            }


        }
        this.selectedCurrency   = selected;
        this.callback           = callback;
        this.selectedPosition           = selectedPosition;
        prepareDataSources();
    }

    @NonNull
    @Override
    public CurrencyCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_cell_currencies, parent, false);
        return new CurrencyCellViewHolder(view);
    }

    private void prepareDataSources() {
        Collections.sort(allCurrencies);
        filter(searchQuery);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyCellViewHolder holder, int position) {
       String bindedCurrency =
                               (getFilteredCurrencies()!= null && getFilteredCurrencies().get(position)!=null )
                               ?
                               getFilteredCurrencies().get(position).getCurrency().getCurrency()
                               :
                               getAllCurrencies().get(position).getCurrency().getCurrency();
        holder.bind(bindedCurrency,position);
    }

    @Override
    public int getItemCount() {
        return getFilteredCurrencies()!=null? getFilteredCurrencies().size(): getAllCurrencies().size();

    }

   private   void setSelection(int newSelection) {

        selectedCurrency = getFilteredCurrencies()!=null ?getFilteredCurrencies().get(newSelection)
                           : getAllCurrencies().get(newSelection);
        if (selectedPosition != NO_SELECTION) {
            notifyItemChanged(selectedPosition);
        }

        selectedPosition = newSelection;
        notifyItemChanged(selectedPosition);
    }

    /**
     * Filter.
     *
     * @param newText the new text
     */
    public void filter(@NonNull String newText) {
        if ( searchQuery == newText ) { return; }
        searchQuery = newText;
        this.filteredCurrencies = Utils.List.filter(getAllCurrencies(), searchQueryFilter);
        notifyDataSetChanged();
    }

    private Utils.List.Filter<LocalizedCurrency> searchQueryFilter = new Utils.List.Filter<LocalizedCurrency>() {

        @Override
        public boolean isIncluded(LocalizedCurrency object) {

            String query = getSearchQuery();
            if ( query.length() == 0 ) {

                return true;
            }

            Pattern pattern = Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE);

            if (pattern.matcher(object.getCurrency().getCurrency()).find()) {

                return true;
            }

            if ( pattern.matcher(object.getLocalizedDisplayName()).find() ) {

                return true;
            }

            return false;
        }
    };


    /**
     * The type Currency cell view holder.
     */
    class CurrencyCellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCurrencyName;

        private ImageView ivCurrencyChecked;

        private CurrencyCellViewHolder(View itemView) {

            super(itemView);
            tvCurrencyName = itemView.findViewById(R.id.tvCurrencyName);
            tvAmountCurrencyName = itemView.findViewById(R.id.tvAmountCurrencyName);
            ivCurrencyChecked = itemView.findViewById(R.id.ivCurrencyChecked);
            ivCurrencyChecked.setImageDrawable(Utils.setImageTint(itemView.getContext(), R.drawable.ic_checkmark_normal, R.color.black));
            ivCurrencyChecked.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(this);
        }

        private void bind(String currencyCode,int _position) {

            tvCurrencyName.setText(getCurrencySelectionString(currencyCode));
            System.out.println("currencyCode"+currencyCode);
            if (currencyCode.equalsIgnoreCase(String.valueOf(selectedCurrency.getCurrency().getCurrency()))) {
               // ivCurrencyChecked.setVisibility(View.VISIBLE);
                selectedPosition = getAdapterPosition();
                tvAmountCurrencyName.setText(selectedCurrency.getCurrency().getSymbol()+selectedCurrency.getCurrency().getAmount());

            } else {
               ivCurrencyChecked.setVisibility(View.INVISIBLE);

                tvAmountCurrencyName.setText("");
            }


        }



        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
          // System.out.println(" get selected adapter position : " + position);
            setSelection(position);

            AmountedCurrency selectedCurrencys = getFilteredCurrencies()!=null ? getFilteredCurrencies().get(position).getCurrency()
                                                : getAllCurrencies().get(position).getCurrency();
           // System.out.println("get user selected currency : " + selectedCurrency.getCurrency());
           //  tvAmountCurrencyName.setText(selectedCurrency.getCurrency().getSymbol()+selectedCurrency.getCurrency().getAmount());
            callback.itemSelected(selectedCurrencys , position);
        }

        //with highlight on search text logic
        private SpannableStringBuilder getCurrencySelectionString(String currencyCode) {
            Currency currency = Utils.getCurrency(currencyCode);
            String symbol = "";
            int currencyNameIndex = 0;

            if (currency != null) {
                symbol = currency.getSymbol(itemView.getContext().getResources().getConfiguration().locale);
            }

            String currencyCodeLowered = currencyCode.toLowerCase();
            String currencyName = Utils.getCurrencyName(currencyCode, currency,itemView.getContext());
//            System.out.println("currencyCodeLowered :  "+ currencyCodeLowered);
//            System.out.println("currencyName :  "+ currencyName);
//            System.out.println("symbol :  "+ symbol);

//            SpannableStringBuilder sb = new SpannableStringBuilder(currencyCode);
            SpannableStringBuilder sb = new SpannableStringBuilder();

            if (!symbol.isEmpty() && !symbol.equalsIgnoreCase(currencyCode)) {
                //sb.append(" ");
                //sb.append(symbol);
            }

            if (!currencyName.isEmpty()) {
//                sb.append(" (");

                currencyNameIndex = sb.length();
                sb.append(currencyName);

//                sb.append(")");
            }
            /*
              SearchQuery check done By Haitham >>> to avoid null pointer exception
             */
            if(searchQuery!=null) {
                //formatting
                if (!currencyName.isEmpty()  && !searchQuery.isEmpty()) {
                    int index = currencyName.toLowerCase().indexOf(searchQuery);
                    while (index >= 0) {
                        Utils.highlightText(itemView.getContext(), sb, currencyNameIndex + index, searchQuery);
                        index = currencyName.toLowerCase().indexOf(searchQuery, index + 1);
                    }
                }

                //search in currency code (not sequentally)
                ArrayList<Integer> indexesToHighlight = new ArrayList<>();
                int index = -1;
                for (char ch : searchQuery.toCharArray()) {
                    index = currencyCodeLowered.indexOf(ch, index + 1);
                    if (index >= 0) {
                        indexesToHighlight.add(index);
                    }
                }
                if (indexesToHighlight.size() == searchQuery.length()) {
                    for (int i : indexesToHighlight) {
                        Utils.highlightText(itemView.getContext(), sb, i);
                    }
                }
            }
            return sb;
        }
    }
}
