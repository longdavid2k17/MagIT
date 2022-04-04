import { rxStompConfig } from './rx-stomp.config';
import {RxStompService} from "./services/rx-stomp.service";

export function rxStompServiceFactory() {
  const rxStomp = new RxStompService();
  rxStomp.configure(rxStompConfig);
  rxStomp.activate();
  return rxStomp;
}
