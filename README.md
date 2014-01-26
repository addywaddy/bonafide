# Bonafide

> bona fide _adjective_

> authentic, genuine, real, true, actual, sterling, sound, legal, legitimate, lawful, **valid**


Bonafide is a tiny validation library for hashmap like objects in clojure.

## Usage

Bonafide doesn't provide any predefined validations but rather allows you to build composable validations of your own.

## Example

Let's say we wanted to implement a validation for checking whether a certain value is present. Using `bonafide` we can do the following:

```clojure

    (defn validate-present [attr & {:as options}]
      (let [
            defaults {:message "Cannot be blank"}

            condition (fn [content options]
                        (not (clojure.string/blank? content)))
            ]
        (build-validation attr options defaults condition)
        )
      )

```

and then use our validation to validate a hashmap:

```clojure

    (def validate (validate-present :name))

    (validate {:name ""})

    =>

    {email: "" :errors {:name "Cannot be blank"}}

```

We can then compose validations to check the presence of several keys:

```clojure

    (def validate (comp
      (validate-present :name)
      (validate-present :email)))

    (validate {:email "" :name ""})

    =>

    {email: "" :errors {:email "Cannot be blank" :name "Cannot be blank"}}

```

Check out the tests for some more examples.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## License

Copyright © 2014 Adam Groves

Distributed under the Eclipse Public License, the same as Clojure.
