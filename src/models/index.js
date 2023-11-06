// @ts-check
import { initSchema } from '@aws-amplify/datastore';
import { schema } from './schema';



const { User, Task } = initSchema(schema);

export {
  User,
  Task
};