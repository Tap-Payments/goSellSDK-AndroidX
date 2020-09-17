package company.tap.gosellapi.open.models;

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
public final class Buyers implements Serializable {
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

    private Buyers(String firstName, String lastName,
                   String email, PhoneNumber phone) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "Customer {" +

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


        private String nestedFirstName;
        private String nestedLastName;
        private String nestedEmail;
        private PhoneNumber nestedPhone;


        /**
         * First name customer builder.
         *
         * @param innerFirstName the inner first name
         * @return the customer builder
         */
        public Buyers.CustomersBuilder firstName(String innerFirstName) {
            this.nestedFirstName = innerFirstName;
            return this;
        }


        /**
         * Last name customer builder.
         *
         * @param innerLastName the inner last name
         * @return the customer builder
         */
        public Buyers.CustomersBuilder lastName(String innerLastName) {
            this.nestedLastName = innerLastName;
            return this;
        }

        /**
         * Email customer builder.
         *
         * @param innerEmail the inner email
         * @return the customer builder
         */
        public Buyers.CustomersBuilder email(String innerEmail) {
            this.nestedEmail = innerEmail;
            return this;
        }

        /**
         * Phone customer builder.
         *
         * @param innerPhone the inner phone
         * @return the customer builder
         */
        public Buyers.CustomersBuilder phone(PhoneNumber innerPhone) {
            this.nestedPhone = innerPhone;
            return this;
        }


        /**
         * Build customer.
         *
         * @return the customer
         */
        public Buyers build() {
            return new Buyers(nestedFirstName, nestedLastName,
                    nestedEmail, nestedPhone);
        }
    }
    ////////////////////////// ############################ End of Builder Region ########################### ///////////////////////

}
