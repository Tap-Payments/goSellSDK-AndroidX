package company.tap.gosellapi.open.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import company.tap.gosellapi.internal.api.models.PhoneNumber;

/**
 * Created by AhlaamK on 9/17/20.
 * <p>
 * Copyright (c) 2020    Tap Payments.
 * All rights reserved.
 **/
// The Type is Buyers
public final class Buyer implements Serializable {
    @SerializedName("id")
    @Expose
    private String identifier;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("phone")
    @Expose
    private PhoneNumber phone;

    private Buyer(String identifier,String firstName, String lastName,
                  String email, PhoneNumber phone) {
        this.identifier = identifier;

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    @Nullable
    public String getFirstName() {
        return this.firstName;
    }
    /**
     * Gets last name.
     *
     * @return the last name
     */
    @Nullable
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    @Nullable
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    @Nullable
    public PhoneNumber getPhone() {
        return this.phone;
    }

    @Override
    public String toString() {
        return "Buyer {" +
                "\n        id =  '" + identifier + '\'' +
                "\n        email =  '" + email + '\'' +
                "\n        first_name =  '" + firstName + '\'' +

                "\n        last_name =  '" + lastName + '\'' +
                "\n        phone  country code =  '" + phone.getCountryCode() + '\'' +
                "\n        phone number =  '" + phone.getNumber() + '\'' +

                "\n    }";
    }

    ////////////////////////// ############################ Start of Builder Region ########################### ///////////////////////

    /**
     * The type Customer builder.
     */
    public static class CustomersBuilder {

        private String nestedIdentifier;
        private String nestedFirstName;
        private String nestedLastName;
        private String nestedEmail;
        private PhoneNumber nestedPhone;

        /**
         * Client app can create a customer object with only customer id
         *
         * @param innerId the inner id
         */
        public CustomersBuilder(String innerId) {
            this.nestedIdentifier = innerId;
        }



        /**
         * First name customer builder.
         *
         * @param innerFirstName the inner first name
         * @return the customer builder
         */
        public Buyer.CustomersBuilder firstName(String innerFirstName) {
            this.nestedFirstName = innerFirstName;
            return this;
        }


        /**
         * Last name customer builder.
         *
         * @param innerLastName the inner last name
         * @return the customer builder
         */
        public Buyer.CustomersBuilder lastName(String innerLastName) {
            this.nestedLastName = innerLastName;
            return this;
        }

        /**
         * Email customer builder.
         *
         * @param innerEmail the inner email
         * @return the customer builder
         */
        public Buyer.CustomersBuilder email(String innerEmail) {
            this.nestedEmail = innerEmail;
            return this;
        }

        /**
         * Phone customer builder.
         *
         * @param innerPhone the inner phone
         * @return the customer builder
         */
        public Buyer.CustomersBuilder phone(PhoneNumber innerPhone) {
            this.nestedPhone = innerPhone;
            return this;
        }


        /**
         * Build customer.
         *
         * @return the customer
         */
        public Buyer build() {
            return new Buyer(nestedIdentifier,nestedFirstName, nestedLastName,
                    nestedEmail, nestedPhone);
        }
    }
    ////////////////////////// ############################ End of Builder Region ########################### ///////////////////////

}
