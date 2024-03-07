#include<iostream>
using namespace std;
int main(){
    int n = 4, i=n;
    while(i > 0){
        for(int j=0; j<= n; j++){
            if(j >= i){
                cout << i+j<<"\n";
                cout<<"i for if:"<<i<<" j for if: "<<j<<"\n";
            }
            else {
                cout << " \n";
                cout<<"i for else : "<<i<<" j for else: "<<j<<"\n";
            }
        }
        cout << endl;
        i --;
    }
    return 0;
}
