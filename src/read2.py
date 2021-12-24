from smartcard.System import readers
import zlib

COMMANDS = {
    'SELECT_MF': [0x00, 0xA4, 0x04, 0x0C, 0x07, 0xD2, 0x76, 0x00, 0x01, 0x44, 0x80, 0x00],
    'SELECT_HCA': [0x00, 0xA4, 0x04, 0x0C, 0x06, 0xD2, 0x76, 0x00, 0x00, 0x01, 0x02],
    'EF_VERSION_1': [0x00, 0xB2, 0x01, 0x84, 0x00],
    'EF_VERSION_2': [0x00, 0xB2, 0x02, 0x84, 0x00],
    'EF_VERSION_3': [0x00, 0xB2, 0x03, 0x84, 0x00],
    'SELECT_FILE_PD': [0x00, 0xB0, 0x81, 0x00, 0x02],
    'SELECT_FILE_VD': [0x00, 0xB0, 0x82, 0x00, 0x08]
}

class HealthCardReader:
    def create_read_command(self, pos, length):
        bpos = [pos >> 8 & 0xFF, pos & 0xFF]
        return [0x00, 0xB0, bpos[0], bpos[1], length]

    def read_file(self, offset, length):
        data = []
        max_read = 0xFC
        pointer = offset
        while len(data) < length:
            bytes_left = length - len(data)
            readlen = bytes_left if bytes_left < max_read else max_read
            data_chunk = self.run_command(self.create_read_command(pointer, readlen))
            pointer += readlen
            data.extend(data_chunk)
        return data

    def run_command(self, adpu):
        data, sw1, sw2 = connection.transmit(adpu)
        if (sw1, sw2) == (0x90, 0x00):
            return data
        raise Exception('Bad Status')

r = readers()
if len(r) < 1:
    raise Exception('No reader found.')
reader = r[0]
connection = reader.createConnection()
connection.connect()

healthcard_reader = HealthCardReader()
healthcard_reader.run_command(COMMANDS['SELECT_MF'])
ef_v_1 = healthcard_reader.run_command(COMMANDS['EF_VERSION_1'])
ef_v_2 = healthcard_reader.run_command(COMMANDS['EF_VERSION_2'])
ef_v_3 = healthcard_reader.run_command(COMMANDS['EF_VERSION_3'])

if ef_v_1 == '3.0.0' and ef_v_2 == '3.0.0' and ef_v_3 == '3.0.2':
    generation = 'G1'
elif ef_v_1 == '3.0.0' and ef_v_2 == '3.0.1' and ef_v_3 == '3.0.3':
    generation = 'G1 plus'
elif ef_v_1 == '4.0.0' and ef_v_2 == '4.0.0' and ef_v_3 == '4.0.2':
    generation = 'G2'

# Selektieren der PD
healthcard_reader.run_command(COMMANDS['SELECT_HCA'])
healthcard_reader.run_command(COMMANDS['SELECT_FILE_PD'])

# Auslesen der ersten beiden Bytes - diese enthalten die Länge der PD
data = healthcard_reader.run_command(healthcard_reader.create_read_command(0x00, 0x02))
pd_length = (data[0] << 8) + data[1]
# Abziehen der ersten beiden Bytes
pd_length -= 0x02

# Nochmaliges Selektieren der PD
healthcard_reader.run_command(COMMANDS['SELECT_MF'])
healthcard_reader.run_command(COMMANDS['SELECT_HCA'])
healthcard_reader.run_command(COMMANDS['SELECT_FILE_PD'])

# Auslesen der komprimierten Daten nach den ersten beiden Bytes, mit der emittelten Länge
patient_data_compressed =  healthcard_reader.read_file(0x02, pd_length)

# Vorbeugen von zlib.error (truncated stream)
patient_data_compressed.extend([0x00] * 16)
patient_data_compressed = bytearray(patient_data_compressed)
patient_data_compressed = bytes(patient_data_compressed)
# Dekomprimieren der Rohdaten
patient_data_xml = zlib.decompress(patient_data_compressed, 15 + 16)

print ("Data: ", patient_data_xml)