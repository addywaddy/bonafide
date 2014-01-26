(ns bonafide.core-test
  (:use clojure.test
        bonafide.core))

(defn- error-on [validation-check]
  (keys (:errors validation-check)))

(defn validate-present [attr & {:as options}]
  (let [
        defaults {:message "Cannot be blank"}

        condition (fn [content options]
                    (not (clojure.string/blank? content)))
        ]
    (build-validation attr options defaults condition)
    )
  )

(defn validate-email [attr & {:as options}]
  (let [
        defaults {:message "Not a valid email address"}
        condition (fn [content options]
                    (and
                     (not (clojure.string/blank? content))
                     (re-find #".+@.+\..+" content)))
        ]
    (build-validation attr options defaults condition)
    )
  )

(defn validate-length [attr & {:as options}]
  (let [
        defaults {:message "Too short"}

        condition (fn [content options]
                    (> (count content) (options :min)))
        ]
    (build-validation attr options defaults condition)
    )
  )

(defn validate-false [attr & {:as options}]
  (let [
        defaults {:message "Is false"}
        condition (fn [content options] (not true))
        ]
    (build-validation attr options defaults condition)
    ))

(deftest default-and-optional-values
  (testing "default message"
    (is (= (:errors ((validate-false :name) {:name "Peter"})) {:name "Is false"})))

  (testing "custom message"
    (is (= (:errors ((validate-false :name :message "WAT") {:name "Peter"})) {:name "WAT"}))))

(deftest multiple-validations-test
  (let [validate (comp
                  (validate-present :name)
                  (validate-email :email)
                  (validate-present :country)
                  (validate-length :country :min 6))]

    (testing "multiple validations are valid"
      (is (= (:errors (validate {:name "John" :email "foo@bar.com" :country "England"})) nil)))

    (testing "multiple validations are invalid"
      (is (= (:errors (validate {})) {:name "Cannot be blank" :email "Not a valid email address" :country ["Too short" "Cannot be blank"]}))
      )
    )
  )
