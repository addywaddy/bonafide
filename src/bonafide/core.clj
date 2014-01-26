(ns bonafide.core)

(declare concat-array)
(declare merge-with-concat)
(declare build-validation)

(defn validate-present [attr & {:as options}]
  (let [
        defaults {:message "Cannot be blank"}

        options (if options (merge defaults options) defaults)

        condition (fn [content options]
                    (not (clojure.string/blank? content)))
        ]
    (build-validation attr options condition)
    )
  )

(defn validate-email [attr & {:as options}]
  (let [
        defaults {:message "Not a valid email address"}

        options (if options (merge defaults options) defaults)

        condition (fn [content options]
                    (and
                     (not (clojure.string/blank? content))
                     (re-find #".+@.+\..+" content)))
        ]
    (build-validation attr options condition)
    )
  )


(defn build-validation [attr options condition]
  (partial (fn [attr options attrs]
             (if (condition (attrs attr) options)
               attrs
               (merge-with merge-with-concat attrs {:errors {attr (options :message)}})
               ))
           attr options))

(defn- merge-with-concat [& args]
  (apply merge-with concat-array args))

(defn- concat-array [& args]
  (flatten (into [] (concat args))))
