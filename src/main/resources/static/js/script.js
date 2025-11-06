// =============================================================
// Shopping Cart – Order Form Validation Script
// =============================================================

$(window).on("load", function () {

    // Check if order form exists
    const form = $("#orders");
    if (form.length === 0) {
        console.warn("⚠️ No #orders form found on this page.");
        return;
    }

    console.log("✅ Order form detected, validation enabled.");

    // -------------------------------------------------------------
    // Custom validation methods
    // -------------------------------------------------------------
    $.validator.addMethod("noSpace", function (value) {
        return value.trim().length > 0;
    }, "This field is required");

    $.validator.addMethod("numericOnly", function (value, element) {
        return this.optional(element) || /^[0-9]+$/.test(value);
    }, "Enter numbers only");

    $.validator.addMethod("lettersOnly", function (value, element) {
        return this.optional(element) || /^[a-zA-Z\\s]+$/.test(value);
    }, "Letters only please");

    // -------------------------------------------------------------
    // Form validation rules and messages
    // -------------------------------------------------------------
    form.validate({
        rules: {
            firstName: { required: true, lettersOnly: true },
            lastName: { required: true, lettersOnly: true },
            email: { required: true, email: true },
            mobileNumber: {
                required: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            address: { required: true, noSpace: true },
            city: { required: true, noSpace: true },
            state: { required: true, noSpace: true },
            pincode: {
                required: true,
                numericOnly: true,
                minlength: 6,
                maxlength: 6
            },
            paymentType: { required: true }
        },

        messages: {
            firstName: "Please enter your first name",
            lastName: "Please enter your last name",
            email: {
                required: "Please enter your email",
                email: "Please enter a valid email address"
            },
            mobileNumber: {
                required: "Please enter your mobile number",
                numericOnly: "Only numbers are allowed",
                minlength: "Must be at least 10 digits",
                maxlength: "Must be 12 digits or fewer"
            },
            address: "Please enter your billing address",
            city: "Please enter your city",
            state: "Please enter your state",
            pincode: {
                required: "Please enter your pincode",
                numericOnly: "Only numbers are allowed",
                minlength: "Must be 6 digits"
            },
            paymentType: "Please select a payment type"
        },

        // -------------------------------------------------------------
        // Error and highlight configuration
        // -------------------------------------------------------------
        errorElement: "div",
        errorClass: "invalid-feedback",

        highlight: function (element) {
            $(element).addClass("is-invalid");
        },
        unhighlight: function (element) {
            $(element).removeClass("is-invalid");
        },
        errorPlacement: function (error, element) {
            if (element.parent(".input-group").length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        },

        // Prevent form submission until all fields are valid
        submitHandler: function (form) {
            form.submit();
        }
    });

    // -------------------------------------------------------------
    // Optional: Show top banner if user tries to submit with errors
    // -------------------------------------------------------------
    form.on("submit", function (e) {
        if (!form.valid()) {
            e.preventDefault();
            if ($("#formAlert").length === 0) {
                form.prepend(`
                    <div id="formAlert" class="alert alert-danger" role="alert">
                        ⚠️ Please fill all required billing details correctly before placing your order.
                    </div>
                `);
                setTimeout(() => $("#formAlert").fadeOut(), 4000);
            }
        }
    });

});
