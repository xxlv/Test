class ShellSort{


public static void main(String [] args)
{
	//step 
	//group
	int i,j,gap;
	int []a={49,38,65,97,76,13,27,49,78,34,12,64,1};
	int len=a.length;
	for (gap=len/2;gap>0;gap/=2){//step for loop

		for (i=0;i<gap;i++){
			for(j=i+gap;j<len;j+=gap){
					// System.out.println("i ="+i+"	j="+j);	

				if(a[j]<a[j-gap]){
					// System.out.println("j ="+a[j]+"	j="+a[j-gap]);	
					int tmp=a[j];	
					int k=j-gap;


					while(k>=0&&a[k]>tmp){//keep k has va

						a[k+gap]=a[k];
						k-=gap;	
					}
					a[k+gap]=tmp;
				}		
			}		
		}
	}

	//print
	for(int x=0;x<len;x++){
		System.out.print(a[x]+"	");
	}
	System.out.println();
}
}
