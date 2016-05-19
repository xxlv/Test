seq = [3,4,9,0,2,5,9,7,1]


1.upto(seq.length-1) do |i|
  if seq[i] < seq[i-1]
    tmp = seq[i]
    j = i-1
    while(j>=0 && tmp<seq[j]) do
      seq[j+1] = seq[j]
      j=j-1
    end
    seq[j+1]=tmp
  end
end


puts seq
