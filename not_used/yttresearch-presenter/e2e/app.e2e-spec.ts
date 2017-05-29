import { PreseneterPage } from './app.po';

describe('preseneter App', () => {
  let page: PreseneterPage;

  beforeEach(() => {
    page = new PreseneterPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
