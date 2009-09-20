16.times {
  N = 2 ** (it+1)
  t = 0..N-1
  h = exp(-t)
  H = fft(h)
  x = ones(1,N)
  Nrep = it<10?5000:100
  tic
  Nrep.times{ y = ifft(fft(x).dot(H)) }
  t = toc
  println "Average FFT convolution $N: ${t/Nrep}"
}