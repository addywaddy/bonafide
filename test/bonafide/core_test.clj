(ns bonafide.core-test
  (:use clojure.test
        bonafide.core))

(defn error-on [validation-check]
  (keys (:errors validation-check))
  )

(deftest validate-present-test
  (testing "attribute is present"
    (is (= (:errors ((validate-present :name) {:name "Peter"})) nil))
    )

  (testing "attribute isn't present"
    (is (= (error-on ((validate-present :name) {})) [:name] ))
    )

  (testing "attribute is empty string"
    (is (= (error-on ((validate-present :name) {:name ""})) [:name]))
    )

  (testing "attribute is blank string"
    (is (= (error-on ((validate-present :name) {:name "\t\n\r "})) [:name]))
    )

  (testing "optional message"
    (is (= (:errors ((validate-present :name :message "WAT") {:name "\t\n\r "})) {:name "WAT"}))
    )
  )

(deftest validate-email-test
  (testing "email is valid"
    (is (= (:errors ((validate-email :email) {:email "foo@bar.com"})) nil))
    )

  (testing "email is invalid"
    (is (= (error-on ((validate-email :email) {:email "foo@b"})) [:email]))
    )

  (testing "optional message"
    (is (= (:errors ((validate-email :email :message "WAT") {:email "foo@"})) {:email "WAT"}))
    )

  (testing "email is invalid"
    (is (= (error-on ((validate-email :email) {})) [:email]))
    )
  )

(deftest multiple-validations-test
  (let [validate (comp
                      (validate-present :email)
                      (validate-email :email)
                      (validate-present :name)
                      )
        ]
    (testing "multiple validations are valid"
      (is (= (:errors (validate {:name "John" :email "foo@bar.com"})) nil))
      )

    (testing "multiple validations are invalid"
      (is (= (:errors (validate {})) {:name "Cannot be blank" :email ["Not a valid email address", "Cannot be blank"]}))
      )
    )
  )


(defn validate-format [attr & {:as options}]
  (build-validation attr (or options {:message "Wrong format"})
                    (fn [content]
                      (clojure.string/blank? (re-find #"ete" content))
                      )
                    )
  )

(deftest custom-validation
  (testing "content matches expression"
    (is (= (:errors ((validate-format :name) {:name "Peter"}) nil)))
  ))
