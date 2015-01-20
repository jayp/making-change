(ns making-change.core-test
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [making-change.core :refer :all]))

(def
  ^{:doc "Make a set of available coins; all sets include the coin 1"}
  mk-coinset
  (gen/fmap #(into #{1} %) (gen/vector gen/s-pos-int)))

(def
  ^{:doc "Pick an amount that is one of the coins in the coinset"}
  pick-amount-from-coinset
  (gen/bind mk-coinset #(gen/tuple (gen/return %) (gen/elements %))))

;; Change must add up (or function is committing fraud! *gasp!*)
(defspec correct-change
         (prop/for-all [coinset mk-coinset
                        amount gen/s-pos-int]
                       (let [change (make-change coinset amount)]
                         (= amount (apply + change)))))

;; If the amount is one of the coins of the coinset, that should be the change
(defspec smallest-change
         (prop/for-all [[coinset amount] pick-amount-from-coinset]
                       (let [change (make-change coinset amount)]
                         (= [amount] change))))

;; The number of coins to change double-amount should be at most twice the
;; number of coins to change amount
(defspec double-amount-change
         (prop/for-all [coinset mk-coinset
                        amount gen/s-pos-int]
                       ;; NOTE: changing dp-change to greedy-change will make
                       ;; this test spec fail
                       (let [mk-change (partial make-change dp-change coinset)
                             change (mk-change amount)
                             double-amount (* amount 2)
                             double-change #spy/d (mk-change double-amount)]
                         (<= (count double-change) (* (count change) 2)))))
