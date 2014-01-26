(ns bonafide.core)

(declare concat-array)
(declare merge-with-concat)
(declare build-validation)

(defn validate-present [attr & {:as options}]
  (build-validation attr (or options {:message "Cannot be blank"})
                    (fn [content options]
                      (not (clojure.string/blank? content))
                      )
                    )
  )

(defn validate-email [attr & {:as options}]
  (build-validation attr (or options {:message "Not a valid email address"})
                    (fn [content options]
                      (and (not (clojure.string/blank? content)) (re-find #".+@.+\..+" content))
                      )
                    )
  )


(defn build-validation [attr options condition]
  (partial (fn [attr options attrs]
             (if (condition (attrs attr) options)
               attrs
               (merge-with merge-with-concat attrs {:errors {attr (options :message)}})
               ))
           attr options
           )
  )


(defn- merge-with-concat [& args]
  (apply merge-with concat-array args)
  )

(defn- concat-array [& args]
  (flatten (into [] (concat args))
  ))
