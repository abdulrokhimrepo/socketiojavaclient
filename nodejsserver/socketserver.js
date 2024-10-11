const http = require('http');
const express = require('express');
const socketIO = require('socket.io');
const cors = require('cors');
const app = express();
const server = http.createServer(app);
const io = socketIO(server);


const satusehat = ['encounterkirim', 'encounterupdate', 'encounterhapus', 'taskid',
  'conditioninsert', 'conditionupdate', 'conditiondelete', 'procedureinsert', 'proceduredelete',
  'procedureupdate'
];
const responsessatusehat = {
  encounterkirim: 'kirimencounter',
  encounterupdate: 'updateencounter',
  encounterhapus: 'hapusencounter',
  taskid: 'kirimtaskid',
  conditioninsert: 'kirimcondition',
  conditionupdate: 'updatecondition',
  conditiondelete: 'deletecondition',
  procedureinsert: 'insertprocedure',
  proceduredelete: 'deleteprocedure',
  procedureupdate: 'updateprocedure'
};

app.use(cors());
io.on('connection', (socket) => {
  console.log('Client connected');

  // Handle your Socket.io events here
  // Listen for the event from the Java app
  socket.on('panggilAdminPoli', (data) => {
    try {
      // Update your database here with data.newQueueNumber
      // Emit the updated data to all connected clients
      io.emit('SiapPanggilAdminPoli', {
        NamaPasien: data.NamaPasien,
        NomorAntrian: data.NomorAntrian
      });
    } catch (error) {
      console.error('Error processing Panggil Admin Poli:', error);
      socket.emit('error', { message: 'An error occurred' });
    }

  });
  socket.on('panggilFarmasi', (data) => {
    try {
      // Update your database here with data.newQueueNumber
      // Emit the updated data to all connected clients
      io.emit('SiapPanggilFarmasi', {
        NamaPasien: data.NamaPasien,
        NomorAntrian: data.NomorAntrian
      });
    } catch (error) {
      console.error('Error processing Panggil Farmasi:', error);
      socket.emit('error', { message: 'An error occurred' });
    }

  });
  socket.on('panggilFarmasi2', (data) => {
    // Update your database here with data.newQueueNumber
    // Emit the updated data to all connected clients
    io.emit('SiapPanggilFarmasi2', {
      NamaPasien: data.NamaPasien
    });
  });
  socket.on('panggilDokter', (data) => {
    // Update your database here with data.newQueueNumber
    try {
      // Emit the updated data to all connected clients
      io.emit('SiapPanggilDokter', {
        NomorAntrian: data.NomorAntrian,
        NamaPasien: data.NamaPasien,
        NamaPoli: data.NamaPoli,
        KodePoli: data.KodePoli,
        KodeDokter: data.KodeDokter,
        NmDokter: data.NmDokter,
        Dokter: data.Dokter

      });
    } catch (error) {
      console.error('Error processing Panggil Dokter:', error);
      socket.emit('error', { message: 'An error occurred' });
    }

  });

  socket.on('DisplayAdmisi', (data) => {
    try {
      io.emit('SiapPanggilAdmisi', {
        NoAntrianAdmisi: data.NoAntrianAdmisi,
        LoketAdmisi: data.LoketAdmisi
      });
    } catch (error) {
      console.error('Error processing PanggilAdmisi:', error);
      socket.emit('error', { message: 'An error occurred' });
    }

  });


  // Loop through each event in the satusehat array
  satusehat.forEach(event => {
    socket.on(event, (data) => {
      console.log(`Received data for event '${event}':`, data);
      // Dynamic handling: Iterate over all keys in the received JSON data
      let responseData = {};
      for (let key in data) {
        if (data.hasOwnProperty(key)) {
          responseData[key] = data[key];
        }
      }
      try {
        // Emit a response back with the processed data and custom message
        io.emit(responsessatusehat[event], responseData);
      } catch (error) {
        console.error('Error processing Satusehat:', error);
        socket.emit('error', { message: 'An error occurred' });
      }


    });
  });

  socket.on('disconnect', () => {
    console.log('Client disconnected');
    socket.removeAllListeners();
  });
});

const port = process.env.PORT || 3000;
server.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});

// Graceful shutdown on SIGINT (Ctrl + C)
process.on('SIGINT', () => {
  console.log('Gracefully shutting down');

  // Stop accepting new connections and close current ones
  server.close(() => {
    console.log('Closed out remaining connections');
    process.exit(0); // Exit process when done
  });

  // Optional: Force exit if server.close() hangs due to open connections
  setTimeout(() => {
    console.error('Forcing server shutdown after 5 seconds');
    process.exit(1); // Non-zero exit code to indicate forced shutdown
  }, 5000); // Force shutdown after 5 seconds
});
