for(m in 1..16) {
  N = 2 ** m
  t = 0..N-1
  h = exp(-t)
  H = fft(h)
  x = ones(1, N)
  Nrep = 500
  tic
  for(i in 1..Nrep) y = ifft(fft(x) .* H)
  println "Average FFT-convolution $N: ${toc/Nrep}"
}