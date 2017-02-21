import { YttresearchFrontendPage } from './app.po';

describe('yttresearch-frontend App', function() {
  let page: YttresearchFrontendPage;

  beforeEach(() => {
    page = new YttresearchFrontendPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
