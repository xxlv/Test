#!/usr/local/bin/sbcl --script

;;;; l love emacs very much then vim or what else
;(defun cos(a)
; (+ 1 1))
;(write(+ 7 8 8))
; hello this is a 
;(cos 45)
;(write-line "Hello world")
;;
;(setq X 10)
;(setq Y 3.3331)
;(print X)
;(print Y)

;;
;(defvar x 10)
;(print x)


;(setq x 110)
;(print x)
;(setTo10 x)
;(print x)

;(print (reverse `(1,(+ 1 2))))  ;(3,1)
;(print (reverse `(1,+( 1 2))))  ;((1 2) NIL 1)

;(defun test-params-num(a b &optional c (d 110 d-supplied-p))
  ;(print d-supplied-p)
  ;(print a)
  ;(print b)
  ;(print c)
;  (print d))



;(dotimes (i 100)
 ; (print i))

;(print #'test-params-num)
;(funcall #'test-params-num 1 2)


;(defvar testvar (list 111 2 3 4))

;(print (first testvar))


;(let ((xx 1110) yy zz)
;  (print yy))




;(defun foo ()
  ;(let ((x 101))

;
;  (setf x 1024)
;  (format t "X: ~d " x)
;   (= x (+ x 1))  
;   (print x)
;   (format t "X: ~d " x))
;    (= (x (+ (x 1))))))


;(foo);
;(print x)



;(cond
; ((> 12 2) 22232)
; (t (print "Hello world")))




;(dotimes (x 10)
;  (print x))


;;;; `

;(dolist (x `(1 2 3 4 5))
;
;  (print x))


;(do ((nums nil) (i 1 (1+ i)))
;    ((> i 10) (nreverse nums))
;    (print i)
;    (push i nums))



;(print (reverse `(1 ,1, (+ 2 1), 3) ))


;(print (macroexpand-1 `(dotimes (p 0 10)
;  (format t "~d" p)))))




;(do ((n 0 (+1 n)))
;    ((> n 10) t))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;(print (macroexpand-1 `(do ((n 0 (1+ n ))
;     (cur 0 next)
;     (next 1 (+ cur next)))
;    ((= 11 n) cur)
;    (print cur))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;; defmacro




;(print (zerop #c(0 0.0)))

;(defun primep (number)
;  (when (> number 1)
;    (loop for fac from 2 to (isqrt number) never (zerop (mod number fac)) )))

;(defun next-prime (number)
;  ((print number)))

;(print (primep 5))
;(print (isqrt 7))



;(do((x 1 100)) ((= x 11) x) (print (1+ x)))


;(defmacro do-primes (var start end)
;  `( do ((,var (next-prime,start)) (next))
;
;  ,@body))


;(do-primes(x 1 10)
;	   (print x))


;(defmacro do-primes((var start end),&body body)
;  `(do ((,var (next-prime,start) (next-prime (1+ , var)))
;	(ending-value ,end))
;       ((>,var ending-value))
;	,@body))
;


;;;;;; ;;;;;;;;;;;;;;;

(defun primes(number)
  (when (> number 1)
    (loop for fac from 2 to (isqrt number) never (zerop (mod number fac))))
  )


;(do ((x 1 (1+ x)))
;    ((> x 10) 10)
;    (print x))


;(print (loop repeat 5
;      for x = 0 then y
;      for y = 1 then (+ x y)
;      collect y)                        


(defun quick_sort(a)
  (print a))




(defun report-result(result form)
  (format t "~:[FAIL~;pass~]...~a~%" result form))


;(defmacro check (form)
;  `(report-result ,form ',form))


;(defmacro combine-results (&body forms-result)
;  
;  (with-gensyms (result)
;    `(let ((,result t))
;       ,@(loop for f in forms-result collect `(unless ,f (setf ,result nil)))
;      ,result)))




;(defmacro check(&body forms)
;  `(combine-results
;     ,@(loop for f in forms collect `(report-result ,f ',f))))







;(print (test+-))


(defun test(a b &optional c e &rest d)
  (format t "~a ~a ~a ~a" a  b c d))

(test 1 2 3 4 1 1)








