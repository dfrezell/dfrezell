
clear ; close all; clc

y = rand(5,1) >= 0.5;
m = length(y);
lambda = 1;
theta = rand(5,1);


reg_theta = ((lambda / (2 * m)) * (theta(2:end)' * theta(2:end))

