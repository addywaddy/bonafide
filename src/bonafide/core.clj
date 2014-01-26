(ns bonafide.core)

(declare concat-array)
(declare merge-with-concat)
(declare build-validation)

(defn validate-present [attr & {:keys [message] :or {message "Cannot be blank"}}]
  (build-validation attr message
                    (fn [content]
                      (clojure.string/blank? content)
                      )
                    )
  )

(defn validate-email [attr & {:keys [message] :or {message "Not a valid email address"}}]
  (build-validation attr message
                    (fn [content]
                      (or (clojure.string/blank? content) (not (re-find #".+@.+\..+" content)))
                      )
                    )
  )


(defn build-validation [attr message condition]
  (partial (fn [attr message attrs]
              (if (condition (attrs attr))
                (merge-with merge-with-concat attrs {:errors {attr message}})
                attrs
                ))
           attr message
            )
  )


(defn- merge-with-concat [& args]
  (apply merge-with concat-array args)
  )

(defn- concat-array [& args]
  (flatten (into [] (concat args))
  ))
