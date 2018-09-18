Hi,

I had a lot of problems with HMM3 and I didn't manage to correct it.
What's happening is that after a lot of iteration my matrix containing my values of beta is full of 0. So
when i calculate Gamma i get division by 0, so NaN and then my matrix A and B get full of 0 too. I have
implemented a scaling like in stamp tutorial, but it's apparently not enough

I asked for help from other people to get
their answer and I think I manage to narrow down my problem to the beta pass. Indeed, I don't have the same 
result of beta pass than some people that passed HMM3. The weird thing is that I seem to get the same value of beta
than other people when I do one iteration on sample1 but not if I do two. My A and B matrix after one iteration seems
right, but with little difference to what other people get (I don't know if it's rounding error). If I try to pute as a file.in the result I get for A and B
I still get the wrong answer for A, B and Beta for one more itération. 

I have trouble to debug because I don't have any example of betaPass. I will try again this afternoon to recode Betapass once again,
but if I don't succeed I will probably really need help.

Thanks you ! 
