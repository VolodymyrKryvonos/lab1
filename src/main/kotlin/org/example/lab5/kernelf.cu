 extern "C"
__global__ void multiply(int *n, float *matrix, float *multiplayer)
{
    for(int i = 0; i<n;i++){
        for(int j = 0; j<n;j++){
            float sum=0;
            for(int k = 0; k<n; k++){
                sum+= matrix[i*n+j] * multiplayer[i*n+k]
            }
            matrix[i*n+j]=sum
        }
    }
}