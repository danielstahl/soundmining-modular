// Mix config
(
s = Server.local;
s.options.outDevice = "16ch Aggregate";
s.options.numOutputBusChannels = 16;
s.options.blockSize = 128;
s.options.sampleRate = 96000;
s.options.numBuffers = 2056;
s.options.memSize = 2.pow(20);
s.dumpOSC(1);
)

// Mix config 2
(
s = Server.local;
s.options.outDevice = "64ch Aggregate";
s.options.numOutputBusChannels = 64;
s.options.blockSize = 128;
s.options.sampleRate = 96000;
s.options.numBuffers = 2056;
s.options.memSize = 2.pow(20);
s.dumpOSC(1);
)


// Ordinary config
(
s = Server.local;
s.options.outDevice = "External Headphones";
s.options.blockSize = 128;
s.options.sampleRate = 96000;
s.options.numBuffers = 2056;
s.options.memSize = 2.pow(20);
s.dumpOSC(1);
)

(
s = Server.local;
s.options.outDevice = "Scarlett 2i2 USB";
s.options.blockSize = 128;
s.options.sampleRate = 96000;
s.options.numBuffers = 2056;
s.options.memSize = 2.pow(20);
s.dumpOSC(1);
)

