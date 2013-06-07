import random, sys, time

num_active_clients = 0

class book:
  def __init__(self, bookname):
    self.bookname = bookname

class book_enum:
  books = [book("Gone with the wind"),
           book("How to kill a mock bird"),
           book("Programming Concurrency"),
           book("Operating Systems"),
           book("Introductory Robotics")]
  @staticmethod
  def random(self):
    return random.choice(self.books)

class inventory:
  def __init__(self):
    self.inventory = {}
    
  def print_work(self, seq_num, job_type, job_id, book, old_quan, new_quan):
    if job_type:
      print 'A restocking work( %s , %s -> %s ) is performed by job id = %s and seqNum = %s' %(book.bookname, old_quan, new_quan, job_id, seq_num)
    else:
      print 'A shipping work( %s , %s -> %s ) is performed by job id = %s and seqNum = %s' %(book.bookname, old_quan, new_quan, job_id, seq_num)
    
  def increase(self, book, quantity, seq_num, job_id):
    if book in self.inventory:
      self.inventory[book] = self.inventory.get(book) + quantity
    else:
      self.inventory[book] = quantity
    self.print_work(seq_num, True, job_id, book, self.inventory.get(book) - quantity, self.inventory.get(book))
      
  def decrease(self, book, quantity, seq_num, job_id):
    if book in self.inventory and self.inventory.get(book) >= quantity:
      self.inventory[book] = self.inventory.get(book) - quantity
      self.print_work(seq_num, False, job_id, book, self.inventory.get(book) + quantity, self.inventory.get(book))
      return True
    else:
      return False
    
class work:
  def __init__(self, book, quantity):
    self.book = book
    self.quantity = quantity
    
def runnable_job(job_id, job_type):
  job_length = yield
  works = []
  while job_length > 0:
    work = yield
    works.append(work)
    job_length -= 1
  ser = yield
  inv = yield
  seq_num = yield
  
  while len(works) != 0:
    work = random.choice(works)
    if job_type:
      inv.increase(work.book, work.quantity, seq_num, job_id)
    else:
      while not inv.decrease(work.book, work.quantity, seq_num, job_id):
        yield
    works.remove(work)
    yield
  
  ser.finish(job_type)

class server:
  def __init__(self, max_num_job, inv):
    self.max_num_job = max_num_job
    self.inv = inv
    self.num_restock_job = 0
    self.num_ship_job = 0
    self.runnable_jobs = []
    
  def process(self, runnable_job, job_type):
    if job_type:
      if self.num_restock_job < self.max_num_job:
        runnable_job.send(self.inv)
        runnable_job.send(self.num_restock_job)
        self.num_restock_job += 1
        self.runnable_jobs.append(runnable_job)
        return True
      else:
        return False
    else:
      if self.num_ship_job < self.max_num_job:
        runnable_job.send(self.inv)
        runnable_job.send(self.num_ship_job)
        self.num_ship_job += 1
        self.runnable_jobs.append(runnable_job)
        return True
      else:
        return False
      
  def finish(self, job_type):
    if job_type:
      self.num_restock_job -= 1
    else:
      self.num_ship_job -= 1
      
  def run(self):
    while True:
      if len(self.runnable_jobs) != 0:
        try:
          job = random.choice(self.runnable_jobs)
          job.next()
          yield
        except StopIteration:
          self.runnable_jobs.remove(job)
      else:
        yield

def client(client_id, ser, life_time, max_job_length):
  global num_active_clients
  t0 = time.time()
  result = True
  while (time.time() - t0) * 1000 < life_time: #convert to milliseconds
    if result:
      generated_job = generate_job(client_id, ser, life_time, max_job_length)
      job = generated_job[0]
      job_type = generated_job[1]
    result = ser.process(job, job_type)
    yield
  num_active_clients -= 1
  
def generate_job(client_id, ser, life_time, max_job_length):
  job_id = client_id * (10 ^ (len(str(life_time))))
  job_type = random.choice([True, False])
  job = runnable_job(job_id, job_type)
  job.next()
  job_length = random.randint(1, max_job_length)
  job.send(job_length)
  while job_length > 0:
    book = book_enum.random(book_enum)
    quantity = random.randint(1, 20)
    w = work(book, quantity)
    job.send(w)
    job_length -= 1
    print_work(job_type, book, quantity, job_id)
  job.send(ser)
  return (job, job_type)

def print_work(job_type, book, quantity, job_id):
  if job_type:
    print 'A restocking work( %s , %s ) will be performed by job id = %s' %(book.bookname, quantity, job_id)
  else:
    print 'A shipping work( %s , %s ) will be performed by job id = %s' %(book.bookname, quantity, job_id)

def main():
  usage = """Main [num_of_clients]
    [client_lif_time] 
    [max_num_of_job_for_each_type] 
    [max_job_length]"""
    
  if len(sys.argv) < 4:
    print '%s' %usage
    exit()
    
  global num_active_clients
  num_active_clients = int(sys.argv[1])
  
  inv = inventory()
  ser = server(int(sys.argv[3]), inv)
  
  tasks = [ser.run()]
  for i in range(int(sys.argv[1])):
    cli = client(i, ser, int(sys.argv[2]), int(sys.argv[4]))
    tasks.append(cli)
    
  while num_active_clients > 1:
    try:
      task = random.choice(tasks)
      task.next()
    except StopIteration:
      tasks.remove(task)
  
if __name__ == '__main__':
  main()