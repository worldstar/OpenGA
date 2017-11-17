n={10,15,20,25,50,100}    ** the order size
Tao={0.1,0.3,0.5,0.7,0.9} ** the tightness factor
R={0.1,0.3,0.5,0.7,0.9}   ** the range factor

For all possible combinations of n,Tao and R we have 10 test instances
For instance; Dataslack_10orders_Tao1R1_1 implies n=10, Tao=0.1, R=0.1, intance=1 


For each test instance we have the parameters:

r(i)= 1 st row of data set
p(i)= 2 nd row of data set
d(i)= 3 rd row of data set
d_bar(i)= 4 th row of data set
e(i)= 5 th row of data set
w(i)= 6 th row of data set
s(i,j)= rest rows of the data set (beginning from 7 th row always)

where i=0,1,2,...,n+1  //0 and n+1 are dummy orders//

r(i)=the release date of order i
p(i)=the processing time of order i
d(i)=the due-date of order i
d_bar(i)=the deadline of order i
e(i)=the revenue of order i
w(i)=the weight of order i
s(i,j)=the sequence dependent set up times

For instance; for Dataslack_10orders_Tao1R1_1

r(i)= [0,10,10,2,4,6,4,5,7,3,5,0]

p(i)= [0,17,19,13,4,13,12,12,2,6,3,0]

d(i)= [0,109,115,106,104,107,109,101,106,103,108,115]

d_bar(i)= [0,111,117,108,105,109,111,103,107,104,109,117]

e(i)= [0,5,19,16,10,12,19,3,16,19,5,0]

w(i)= [0,2.5,9.5,8,10,6,9.5,1.5,16,19,5,0]

s(i,j)= [0,7,10,3,2,4,3,8,4,6,7,0
         0,0,10,9,6,3,7,3,6,7,2,0
         0,2,0,6,9,7,7,5,10,2,9,0
         0,9,2,0,7,6,5,4,9,7,3,0
         0,10,2,2,0,10,7,5,10,5,6,0
         0,6,6,8,6,0,5,7,9,10,9,0
         0,9,6,5,6,2,0,7,2,7,10,0
         0,3,4,9,2,9,7,0,9,3,4,0
         0,7,5,8,6,4,3,3,0,6,4,0
         0,6,2,3,10,2,9,3,10,0,7,0
         0,9,7,3,4,7,7,2,3,2,0,0
         0,0,0,0,0,0,0,0,0,0,0,0]

